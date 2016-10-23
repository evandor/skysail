package io.skysail.api.weaving;

public class Interceptor {

    private static final MethodInterceptorManager methodInterceptorManager = new MethodInterceptorManager();

    private Interceptor() {}

    public static boolean beforeMethodInvocation(String str) {
        return Interceptor.methodInterceptorManager.beforeInvocation(str);
    }

}
