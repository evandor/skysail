package io.skysail.server.ext.weaving.methodinvocations.impl;

import java.lang.reflect.Method;
import java.util.Map;

public interface MethodInterceptor {
	
	boolean interestedIn(Method method, Map<String, Object> context);

	BeforeInvocationDecision beforeInvocation(BeforeMethodInvocation invocation) throws Throwable;

	Object afterInvocation(AfterMethodInvocation invocation) throws Throwable;

	Object afterThrowable(AfterMethodException invocation) throws Throwable;

	interface MethodInvocation {

		Map<String, Object> getInterceptorContext();

		Map<String, Object> getInvocationContext();

		Method getMethod();

		Object getObject();

		Object[] getArguments();
	}

	interface BeforeMethodInvocation extends MethodInvocation {

		BeforeInvocationDecision continueInvocation();

		BeforeInvocationDecision abort(Object returnValue);
	}

	interface BeforeInvocationDecision {
	}

	interface AfterMethodInvocation extends MethodInvocation {

		Object getReturnValue();
	}

	interface AfterMethodException extends MethodInvocation {

		Throwable getThrowable();
	}
}
