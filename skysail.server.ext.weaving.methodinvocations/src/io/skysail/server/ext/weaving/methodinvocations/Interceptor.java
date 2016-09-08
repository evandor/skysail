package io.skysail.server.ext.weaving.methodinvocations;

import io.skysail.server.ext.weaving.methodinvocations.impl.MethodInterceptorManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Interceptor {

    private static final MethodInterceptorManager methodInterceptorManager = new MethodInterceptorManager();

//    public static Object getValueForField(long moduleId, Class<?> type, String fieldName) {
//        // Module module =
//        // Activator.getModuleManager().getModule(moduleId).get();
//        // TypeDescriptor typeDescriptor = module.getTypeDescriptor(type);
//        // if (typeDescriptor == null) {
//        // throw new ComponentCreateException(
//        // "could not find descriptor for type '" + type.getName() + "' in
//        // module " + module, type, module);
//        // } else {
//        // return typeDescriptor.getValueForField(fieldName);
//        // }
//        return null;
//    }

//    public static boolean beforeInvocation(MethodEntry methodEntry, Object object, Object[] arguments)
//            throws Throwable {
//        return Interceptor.methodInterceptorManager.beforeInvocation(methodEntry, object, arguments);
//    }

    public static boolean beforeInvocation2(String str) throws Throwable {
        return Interceptor.methodInterceptorManager.beforeInvocation2(str);
    }

    public static void cleanup(MethodEntry methodEntry) throws Throwable {
        Interceptor.methodInterceptorManager.cleanup(methodEntry);
    }

}
