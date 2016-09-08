package io.skysail.server.ext.weaving.methodinvocations;

import io.skysail.server.ext.weaving.methodinvocations.impl.MethodInterceptorManager;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Interceptor {

    private static final MethodInterceptorManager methodInterceptorManager = new MethodInterceptorManager();

    public static boolean beforeInvocation2(String str) throws Throwable {
        return Interceptor.methodInterceptorManager.beforeInvocation2(str);
    }

}
