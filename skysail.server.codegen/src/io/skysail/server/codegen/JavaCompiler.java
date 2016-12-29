package io.skysail.server.codegen;

import org.osgi.framework.BundleContext;

public interface JavaCompiler {

    boolean compile(BundleContext bundleContext);
    
    Class<?> getClass(String className) throws ClassNotFoundException;
    
    void reset();

    CompiledCode collect(String className, String entityCode);

    void collectSource(String className, String entityCode);

}
