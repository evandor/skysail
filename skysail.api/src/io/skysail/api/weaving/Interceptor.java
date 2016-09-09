package io.skysail.api.weaving;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Interceptor {

    private static final MethodInterceptorManager methodInterceptorManager = new MethodInterceptorManager();

    public static boolean beforeMethodInvocation(String str) throws Throwable {
        return Interceptor.methodInterceptorManager.beforeInvocation(str);
    }

}
