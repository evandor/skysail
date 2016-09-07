package io.skysail.server.ext.weaving.methodinvocations.impl;

import static java.lang.String.format;
import static javassist.Modifier.isAbstract;
import static javassist.Modifier.isFinal;
import static javassist.Modifier.isNative;
import static javassist.Modifier.isStatic;
import static javassist.bytecode.AccessFlag.BRIDGE;
import static javassist.bytecode.AccessFlag.SYNTHETIC;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.osgi.framework.hooks.weaving.WeavingException;
import org.osgi.framework.hooks.weaving.WeavingHook;
import org.osgi.framework.hooks.weaving.WovenClass;
import org.osgi.service.component.annotations.Component;

import io.skysail.server.ext.weaving.methodinvocations.MethodEntry;
import io.skysail.server.ext.weaving.methodinvocations.ModulesSpi;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true)
@Slf4j
public class InvocationCounterWeavingHook implements WeavingHook {

	private static final String WEAVING_PACKAGE = "io.skysail.server.ext.weaving";

	private static final AtomicInteger clsCounter = new AtomicInteger(0);

	@Override
	public void weave(WovenClass wovenClass) {
		if (isInstrumented(wovenClass.getClassName())) {
			CtClass ctClass = loadConcreteClass(wovenClass);
			if (ctClass != null) {
				log.info("weaving class #" + clsCounter.incrementAndGet() + ": " + wovenClass.getClassName());

				// weaveFieldInjections(wovenClass, ctClass);

				weaveMethodInterception(ctClass);

				if (ctClass.isModified()) {
					try {
						wovenClass.setBytes(ctClass.toBytecode());
					} catch (Throwable e) {
						log.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	private void weaveFieldInjections(WovenClass wovenClass, CtClass ctClass) {
		// list of constructors that should be weaved with our modifications
		try {
			Collection<CtConstructor> superCallingConstructors = getSuperCallingConstructors(ctClass);
			for (CtField field : ctClass.getDeclaredFields()) {
				int modifiers = field.getModifiers();
				if (!isStatic(modifiers) && !isFinal(modifiers)) {
					/*
					 * if( field.hasAnnotation(
					 * org.mosaic.modules.Component.class ) ||
					 * field.hasAnnotation( Service.class ) ) { addBeforeBody(
					 * superCallingConstructors, process(
					 * "this.%s = (%s) ModulesSpi.getValueForField( %dl, %s.class, \"%s\" );"
					 * , field.getName(), field.getType().getName(),
					 * wovenClass.getBundleWiring().getBundle().getBundleId(),
					 * ctClass.getName(), field.getName() ) ); }
					 */
				}
			}
			// } catch (NotFoundException ignore) {
			// // simply not weaving the class; it won't load anyway...
		} catch (WeavingException e) {
			throw e;
		} catch (Throwable e) {
			throw new WeavingException(
					"could not weave fields of '" + wovenClass.getClassName() + "': " + e.getMessage(), e);
		}
	}

	private Collection<CtConstructor> getSuperCallingConstructors(CtClass ctClass) throws CannotCompileException {
		List<CtConstructor> initializers = new LinkedList<>();
		CtConstructor[] declaredConstructors = ctClass.getDeclaredConstructors();
		for (CtConstructor ctor : declaredConstructors) {
			if (ctor.callsSuper()) {
				initializers.add(ctor);
			}
		}
		return initializers;
	}

	private void weaveMethodInterception(CtClass ctClass) {
		try {
			// if( isSubtypeOf( ctClass, MethodInterceptor.class.getName() ) )
			// {
			//// Logger logger = LoggerFactory.getLogger( ctClass.getName() );
			//// logger.info( "Class '{}' will not be weaved for method
			// interception, because it implements {}",
			//// ctClass.getName(), MethodInterceptor.class.getName() );
			// return;
			// }

			// creates a shared map of method entries for this class
			Map<CtMethod, Long> methodIds = createClassInitializer(ctClass);

			for (CtMethod method : ctClass.getDeclaredMethods()) {
				Long id = methodIds.get(method);
				if (id != null) {
					weaveMethodForInterception(id, method);
				}
			}
		} catch (WeavingException e) {
			log.error(e.getMessage(), e);
		} catch (Throwable e) {
			log.error("could not weave interception for methods in '" + ctClass.getName() + "': " + e.getMessage(), e);
		}
	}

	private void weaveMethodForInterception(long id, CtMethod method) {
		try {
			// add code that invokes the 'after' interception
			String afterSuccessSrc = process(
					getReturnStatement(method.getReturnType(), "ModulesSpi.afterSuccessfulInvocation( ($w)$_ )"));
			//method.insertAfter(afterSuccessSrc, false);

			// add code that catches exceptions
			String catchSrc = process(
					"{                                                                                         \n"
							+ "   " + getReturnStatement(method.getReturnType(), "ModulesSpi.afterThrowable( $e )")
							+ "\n"
							+ "   throw $e;                                                                              \n"
							+ "}                                                                                         \n");
			//method.addCatch(catchSrc, method.getDeclaringClass().getClassPool().get(Throwable.class.getName()), "$e");

			// add code that invokes the 'before' interception
			String beforeSrc = process(
					"{                                                                                      \n"
							+ "   MethodEntry __myEntry = (MethodEntry) __METHOD_ENTRIES.get( Long.valueOf( %dl ) );  \n"
							+ "   if( !ModulesSpi.beforeInvocation( __myEntry, %s, $args ) )                          \n"
							+ "   {                                                                                   \n"
							+ "       %s;                                                                             \n"
							+ "   }                                                                                   \n"
							+ "}                                                                                      \n",
					id, isStatic(method.getModifiers()) ? "null" : "this",
					getReturnStatement(method.getReturnType(), "ModulesSpi.afterAbortedInvocation()"));
			method.insertBefore(beforeSrc);

			// add code that invokes the 'after' interception
			String afterSrc = process("ModulesSpi.cleanup( (MethodEntry)__METHOD_ENTRIES.get( Long.valueOf( %dl ) ) );",
					id);
			//method.insertAfter(afterSrc, true);
		} catch (NotFoundException ignore) {
			// simply not weaving the class; it won't load anyway...
		} catch (WeavingException e) {
			log.error(e.getMessage(), e);
		} catch (Throwable e) {
			log.error("could not weave method '" + method.getLongName() + "' of '"
					+ method.getDeclaringClass().getName() + "': " + e.getMessage(), e);
		}
	}

	private String getReturnStatement(CtClass returnType, String valueStmt) {
		switch (returnType.getName()) {
		case "void":
			return valueStmt + ";";
		case "boolean":
			return format("return ( (Boolean) %s ).booleanValue();", valueStmt);
		case "byte":
			return format("return ( (Number) %s ).byteValue();", valueStmt);
		case "char":
			return format("return ( (Character) %s ).charValue();", valueStmt);
		case "double":
			return format("return ( (Number) %s ).doubleValue();", valueStmt);
		case "float":
			return format("return ( (Number) %s ).floatValue();", valueStmt);
		case "int":
			return format("return ( (Number) %s ).intValue();", valueStmt);
		case "long":
			return format("return ( (Number) %s ).longValue();", valueStmt);
		case "short":
			return format("return ( (Number) %s ).shortValue();", valueStmt);
		default:
			return format("return (%s) %s;", returnType.getName(), valueStmt);
		}
	}

	private Map<CtMethod, Long> createClassInitializer(CtClass ctClass) {
		try {
			CtConstructor classInitializer = ctClass.getClassInitializer();
			if (classInitializer == null) {
				classInitializer = ctClass.makeClassInitializer();
			}
			ctClass.addField(CtField
					.make("public static final java.util.Map __METHOD_ENTRIES = new java.util.HashMap(100);", ctClass));

			// this counter will increment for each declared method in this
			// class
			// ie. each methods receives a unique ID (in the context of this
			// class..)
			long id = 0;

			// iterate declared methods, and for each method, add code that
			// populates the __METHOD_ENTRIES static map
			// with a MethodEntry for that method. The entry will receive the
			// method's unique ID.
			// in addition to generating the code to populate the class's method
			// entries map, we save the method IDs
			// mapping in a local map here and return it - so that the code
			// weaved to our methods will use that id to
			// fetch method entry on runtime when methods are invoked.

			StringBuilder methodIdMapSrc = new StringBuilder();
			Map<CtMethod, Long> methodIds = new HashMap<>();
			for (CtMethod method : ctClass.getDeclaredMethods()) {
				int acc = AccessFlag.of(method.getModifiers());
				if ((acc & BRIDGE) == 0 && (acc & SYNTHETIC) == 0) {
					int modifiers = method.getModifiers();
					if (!isAbstract(modifiers) && !isNative(modifiers)) {
						long methodId = id++;

						methodIdMapSrc.append(process(
								"%s.__METHOD_ENTRIES.put(                           \n"
										+ "   Long.valueOf( %dl ),                            \n"
										+ "   new MethodEntry( %dl, %s.class, \"%s\", %s )    \n"
										+ ");                                                 \n",
								ctClass.getName(), methodId, methodId, method.getDeclaringClass().getName(),
								method.getName(), getMethodParametersArrayString(method)));

						methodIds.put(method, methodId);
					}
				}
			}
			System.out.println(methodIdMapSrc);
			classInitializer.insertBefore("{\n" + methodIdMapSrc.toString() + "}");
			return methodIds;
		} catch (NotFoundException ignore) {
			// simply not weaving the class; it won't load anyway...
			return Collections.emptyMap();
		} catch (Throwable e) {
			log.error("could not create class initializer for class '" + ctClass.getName() + "': " + e.getMessage(), e);
		}
		return null;
	}

	private String getMethodParametersArrayString(CtMethod method) throws NotFoundException {
		CtClass[] parameterTypes = method.getParameterTypes();
		if (parameterTypes == null || parameterTypes.length == 0) {
			return "new String[0]";
		}

		StringBuilder methodParameterTypes = new StringBuilder();
		for (CtClass ctParamType : parameterTypes) {
			if (methodParameterTypes.length() > 0) {
				methodParameterTypes.append(", ");
			}
			methodParameterTypes.append("\"").append(ctParamType.getName()).append("\"");
		}
		return "new String[] { " + methodParameterTypes + "}";
	}

	private String process(String code, Object... args) {
		return String.format(code, args).replace(MethodEntry.class.getSimpleName(), MethodEntry.class.getName())
				.replace(ModulesSpi.class.getSimpleName(), ModulesSpi.class.getName());
	}

	private CtClass loadConcreteClass(WovenClass wovenClass) {
		try {
			ClassPool classPool = new ClassPool(false);
			classPool.appendSystemPath();
			classPool.appendClassPath(new LoaderClassPath(InvocationCounterWeavingHook.class.getClassLoader()));
			classPool.appendClassPath(new LoaderClassPath(wovenClass.getBundleWiring().getClassLoader()));

			CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(wovenClass.getBytes()));
			if (ctClass.isArray() || ctClass.isAnnotation() || ctClass.isEnum() || ctClass.isInterface()) {
				return null;
			} else {
				return ctClass;
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	private boolean isInstrumented(String className) {
		if (className.startsWith(WEAVING_PACKAGE)) {
			return false;
		}
		return className.contains(".skysail.api");
	}

	private static String[] toString(Class<?>[] interfaces) {
		String[] names = new String[interfaces.length];
		int i = 0;
		for (Class clazz : interfaces) {
			names[i++] = clazz.getName();
		}
		return names;
	}

//	private static ServiceRegistration proxyService(Bundle bundleSource, Class<?>[] interfaces, Properties prop,
//			ClassLoader cl, InvocationHandler proxy) {
//
//		prop.put(PROXY, true);
//		Object loggerProxy = Proxy.newProxyInstance(cl, interfaces, proxy);
//		return bundleSource.getBundleContext().registerService(toString(interfaces), loggerProxy, (Dictionary) prop);
//
//	}

}