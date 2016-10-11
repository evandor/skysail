package io.skysail.server.ext.weaving.methodinvocations.impl;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.osgi.framework.hooks.weaving.WeavingHook;
import org.osgi.framework.hooks.weaving.WovenClass;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.Designate;

import io.skysail.api.weaving.Interceptor;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import lombok.extern.slf4j.Slf4j;

@Component(
        immediate = true,
        configurationPid = "invocationcounter",
        configurationPolicy = ConfigurationPolicy.REQUIRE
)
@Designate(ocd = InvocationCounterConfig.class)
@Slf4j
public class InvocationCounterWeavingHook implements WeavingHook {

    private static final String WEAVING_PACKAGE = "io.skysail.api.weaving";

    private static final AtomicInteger clsCounter = new AtomicInteger(0);

    private static InvocationCounterConfig config;

    @Activate
    public void activate(InvocationCounterConfig config) {
        InvocationCounterWeavingHook.config = config;
    }

    @Deactivate
    protected void deactivate(ComponentContext ctxt) {
        InvocationCounterWeavingHook.config = null;
    }

    public static List<String> getConfigData() {
        List<String> result = new ArrayList<>();
        if (config != null) {
            result.add("Package Pattern: " + config.packagePattern());
        }
        return result;
    }

    @Override
    public void weave(WovenClass wovenClass) {
        if (!isInstrumented(wovenClass.getClassName())) {
            return;
        }
        loadConcreteClass(wovenClass).ifPresent(ctClass -> {
            log.info("weaving class #" + clsCounter.incrementAndGet() + ": " + wovenClass.getClassName());
            weaveMethodInterception(ctClass);
            setBytes(wovenClass, ctClass);
        });
    }


    private boolean isInstrumented(String className) {
        if (className.startsWith(WEAVING_PACKAGE)) {
            return false;
        }
        return className.contains(config.packagePattern());
    }

    private Optional<CtClass> loadConcreteClass(WovenClass wovenClass) {
        try {
            ClassPool classPool = new ClassPool(false);
            classPool.appendSystemPath();
            classPool.appendClassPath(new LoaderClassPath(InvocationCounterWeavingHook.class.getClassLoader()));
            classPool.appendClassPath(new LoaderClassPath(wovenClass.getBundleWiring().getClassLoader()));

            CtClass ctClass = classPool.makeClass(new ByteArrayInputStream(wovenClass.getBytes()));
            if (!ctClass.isArray() && !ctClass.isAnnotation() && !ctClass.isEnum() && !ctClass.isInterface()) {
                return Optional.of(ctClass);
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        return Optional.empty();
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
        for (CtMethod method : ctClass.getDeclaredMethods()) {
            weaveMethodForInterception(method);
        }
    }

    private void weaveMethodForInterception(CtMethod method) {
        if (Modifier.isAbstract(method.getModifiers())) {
            return;
        }
        try {
            if (method.getReturnType().getName().equals(Stream.class.getName())) {
                return;
            }
            String methodIdentifier = new StringBuilder(method.getDeclaringClass().getName()).append("#")
                    .append(method.getLongName()).toString();
            method.insertBefore("{ " + Interceptor.class.getName() + ".beforeMethodInvocation( \"" + methodIdentifier
                    + "\" ); }\n");
        } catch (Throwable e) {
            log.error("could not weave method '" + method.getLongName() + "' in '"
                    + method.getDeclaringClass().getName() + "': " + e.getMessage(), e);
        }
    }

}