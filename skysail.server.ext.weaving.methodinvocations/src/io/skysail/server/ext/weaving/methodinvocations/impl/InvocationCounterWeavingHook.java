package io.skysail.server.ext.weaving.methodinvocations.impl;

import static java.lang.String.format;
import static javassist.Modifier.isAbstract;
import static javassist.Modifier.isNative;
import static javassist.Modifier.isStatic;
import static javassist.bytecode.AccessFlag.BRIDGE;
import static javassist.bytecode.AccessFlag.SYNTHETIC;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.osgi.framework.hooks.weaving.WeavingException;
import org.osgi.framework.hooks.weaving.WeavingHook;
import org.osgi.framework.hooks.weaving.WovenClass;
import org.osgi.service.component.annotations.Component;

import io.skysail.server.ext.weaving.methodinvocations.Interceptor;
import io.skysail.server.ext.weaving.methodinvocations.MethodEntry;
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
		if (!isInstrumented(wovenClass.getClassName())) {
			return;
		}
		CtClass ctClass = loadConcreteClass(wovenClass);
		if (ctClass == null) {
			return;
		}
		log.info("weaving class #" + clsCounter.incrementAndGet() + ": " + wovenClass.getClassName());
		weaveMethodInterception(ctClass);
		setBytes(wovenClass, ctClass);
	}
	
	private boolean isInstrumented(String className) {
		if (className.startsWith(WEAVING_PACKAGE)) {
			return false;
		}
		return className.contains(".skysail.api");
	}
	
	private CtClass loadConcreteClass(WovenClass wovenClass) {
		try {
			ClassPool classPool = new ClassPool(false);
			classPool.appendSystemPath();
			classPool.appendClassPath(new LoaderClassPath(InvocationCounterWeavingHook.class.getClassLoader()));
			classPool.appendClassPath(new LoaderClassPath(wovenClass.getBundleWiring().getClassLoader()));

			CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(wovenClass.getBytes()));
			if (!ctClass.isArray() && !ctClass.isAnnotation() && !ctClass.isEnum() && !ctClass.isInterface()) {
				return ctClass;
			}
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	private void setBytes(WovenClass wovenClass, CtClass ctClass) {
		if (ctClass.isModified()) {
			try {
				wovenClass.setBytes(ctClass.toBytecode());
			} catch (Throwable e) {
				log.error(e.getMessage(), e);
			}
		}
	}

	private void weaveMethodInterception(CtClass ctClass) {
		try {
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
			String beforeSrc = process(
					"{                                                                                      \n"
							+ "   MethodEntry __myEntry = (MethodEntry) __METHOD_ENTRIES.get( Long.valueOf( %dl ) );  \n"
							+ "   if( !Interceptor.beforeInvocation( __myEntry, %s, $args ) )                          \n"
							+ "   {                                                                                   \n"
							+ "       %s;                                                                             \n"
							+ "   }                                                                                   \n"
							+ "}                                                                                      \n",
					id, isStatic(method.getModifiers()) ? "null" : "this",
					getReturnStatement(method.getReturnType(), "Interceptor.afterAbortedInvocation()"));
			method.insertBefore(beforeSrc);

		} catch (NotFoundException ignore) {
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

			long id = 0;
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
				.replace(Interceptor.class.getSimpleName(), Interceptor.class.getName());
	}

	
	

}