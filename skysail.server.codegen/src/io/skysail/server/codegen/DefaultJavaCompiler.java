package io.skysail.server.codegen;


import org.osgi.framework.BundleContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultJavaCompiler implements JavaCompiler {

    @Override
    public boolean compile(BundleContext bundleContext) {
        try {
            InMemoryCodeGenerator.compile(bundleContext);
            return InMemoryCodeGenerator.isCompiledSuccessfully();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Class<?> getClass(String className) throws ClassNotFoundException {
        return InMemoryCodeGenerator.getClass(className);
    }

    @Override
    public void reset() {
        InMemoryCodeGenerator.reset();
    }

    @Override
    public CompiledCode collect(String className, String entityCode) {
        return InMemoryCodeGenerator.collect(className, entityCode);
    }

    @Override
    public void collectSource(String className, String entityCode) {
        InMemoryCodeGenerator.collectSource(className, entityCode);
    }

}
