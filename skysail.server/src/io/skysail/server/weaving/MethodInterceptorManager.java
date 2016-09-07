//package io.skysail.server.weaving;
//
//import com.google.common.cache.CacheBuilder;
//import com.google.common.cache.CacheLoader;
//import com.google.common.cache.LoadingCache;
//import com.google.common.util.concurrent.UncheckedExecutionException;
//import java.lang.reflect.Method;
//import java.util.*;
//import org.osgi.framework.ServiceReference;
//import org.osgi.util.tracker.ServiceTracker;
//import org.osgi.util.tracker.ServiceTrackerCustomizer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import static java.util.Arrays.copyOf;
//
//final class MethodInterceptorManager {
//	private static final Logger LOG = LoggerFactory.getLogger(MethodInterceptorManager.class);
//
//	private final MethodInterceptor.BeforeInvocationDecision continueDecision = new ContinueDecision();
//
//	private final MethodInterceptor.BeforeInvocationDecision abortDecision = new AbortDecision();
//
//	private final LoadingCache<Method, List<InterestedMethodInterceptorEntry>> methodInterceptorsCache = CacheBuilder
//			.newBuilder().concurrencyLevel(100).initialCapacity(100000).weakKeys()
//			.build(new CacheLoader<Method, List<InterestedMethodInterceptorEntry>>() {
//
//				@Override
//				public List<InterestedMethodInterceptorEntry> load(Method method) throws Exception {
//					Object[] services = null;// MethodInterceptorManager.this.interceptorsTracker.getServices();
//					if (services == null || services.length == 0) {
//						return Collections.emptyList();
//					}
//
//					List<InterestedMethodInterceptorEntry> interceptors = null;
//					for (Object service : services) {
//						Map<String, Object> context = new HashMap<>();
//
//						MethodInterceptor interceptor = (MethodInterceptor) service;
//						if (interceptor.interestedIn(method, context)) {
//							if (interceptors == null) {
//								interceptors = new LinkedList<>();
//							}
//							interceptors.add(new InterestedMethodInterceptorEntry(interceptor, context));
//						}
//					}
//					return interceptors == null ? Collections.<InterestedMethodInterceptorEntry>emptyList()
//							: interceptors;
//				}
//			});
//
////	private final ServiceTracker<MethodInterceptor, MethodInterceptor> interceptorsTracker = new ServiceTracker<>(
////			bundleContext(getClass()), MethodInterceptor.class,
////			new ServiceTrackerCustomizer<MethodInterceptor, MethodInterceptor>() {
////				@Override
////				public MethodInterceptor addingService(ServiceReference<MethodInterceptor> reference) {
////					MethodInterceptorManager.this.methodInterceptorsCache.invalidateAll();
////					// noinspection ConstantConditions
////					return bundleContext(getClass()).getService(reference);
////				}
////
////				@Override
////				public void modifiedService(ServiceReference<MethodInterceptor> reference, MethodInterceptor service) {
////					MethodInterceptorManager.this.methodInterceptorsCache.invalidateAll();
////				}
////
////				@Override
////				public void removedService(ServiceReference<MethodInterceptor> reference, MethodInterceptor service) {
////					MethodInterceptorManager.this.methodInterceptorsCache.invalidateAll();
////				}
////			});
//
//	private final ThreadLocal<Deque<InvocationContext>> contextHolder = new ThreadLocal<Deque<InvocationContext>>() {
//		@Override
//		protected Deque<InvocationContext> initialValue() {
//			return new LinkedList<>();
//		}
//	};
//
//	MethodInterceptorManager() {
//		//this.interceptorsTracker.open();
//	}
//
//	public boolean beforeInvocation(MethodEntry methodEntry, Object object, Object[] arguments) throws Throwable {
//		// create context for method invocation and push to stack
//		InvocationContext context = new InvocationContext(methodEntry, object, arguments);
//		this.contextHolder.get().push(context);
//
//		// invoke interceptors "before" action, returning true if method should
//		// proceed, false if circumvent method and request a call to "after"
//		// action
//		return context.beforeInvocation();
//	}
//
//	public Object afterAbortedInvocation() throws Throwable {
//		return this.contextHolder.get().peek().afterInvocation();
//	}
//
//	public Object afterSuccessfulInvocation(Object returnValue) throws Throwable {
//		InvocationContext context = this.contextHolder.get().peek();
//		context.returnValue = returnValue;
//		return context.afterInvocation();
//	}
//
//	public Object afterThrowable(Throwable throwable) throws Throwable {
//		InvocationContext context = this.contextHolder.get().peek();
//		context.throwable = throwable;
//		return context.afterInvocation();
//	}
//
//	public void cleanup(MethodEntry methodEntry) {
//		Deque<InvocationContext> deque = this.contextHolder.get();
//		if (deque.isEmpty()) {
//			LOG.error("STACK EMPTY! received method: {}", methodEntry);
//		} else if (!deque.peek().methodEntry.equals(methodEntry)) {
//			LOG.error("STACK DIRTY!\n" + "    On stack: {}\n" + "    Received: {}", deque.peek().methodEntry,
//					methodEntry);
//		} else {
//			deque.pop();
//		}
//	}
//
//	private class InterestedMethodInterceptorEntry {
//
//		private final MethodInterceptor target;
//
//		private final Map<String, Object> interceptorContext;
//
//		private InterestedMethodInterceptorEntry(MethodInterceptor target, Map<String, Object> interceptorContext) {
//			this.target = target;
//			this.interceptorContext = interceptorContext;
//		}
//	}
//
//	private class InvocationContext extends HashMap<String, Object> {
//
//		private final MethodEntry methodEntry;
//
//		private final Object object;
//
//		private final Object[] arguments;
//
//		private final List<InterestedMethodInterceptorEntry> invokedInterceptors = new LinkedList<>();
//
//		private Map<InterestedMethodInterceptorEntry, Map<String, Object>> interceptorInvocationContexts = null;
//
//		private BeforeMethodInvocationImpl beforeInvocation;
//
//		private AfterMethodInvocationImpl afterInvocation;
//
//		private AfterMethodExceptionImpl afterThrowable;
//
//		private Throwable throwable;
//
//		private Object returnValue;
//
//		private InvocationContext(MethodEntry methodEntry, Object object, Object[] arguments)
//				throws ClassNotFoundException {
//			super(5);
//			this.methodEntry = methodEntry;
//			this.object = object;
//			this.arguments = arguments;
//		}
//
//		private boolean beforeInvocation() {
//			Method method = this.methodEntry.getMethod();
//
//			List<InterestedMethodInterceptorEntry> entries;
//			try {
//				entries = methodInterceptorsCache.getUnchecked(method);
//			} catch (UncheckedExecutionException e) {
//				Throwable cause = e.getCause();
//				if (cause instanceof RuntimeException) {
//					throw (RuntimeException) cause;
//				} else {
//					throw e;
//				}
//			}
//
//			for (InterestedMethodInterceptorEntry interceptorEntry : entries) {
//				MethodInterceptor interceptor = interceptorEntry.target;
//				try {
//					BeforeMethodInvocationImpl invocation = getBeforeInvocation();
//					invocation.methodInterceptorEntry = interceptorEntry;
//
//					// invoke interceptor
//					MethodInterceptor.BeforeInvocationDecision decision = interceptor.beforeInvocation(invocation);
//
//					// if interceptor succeeded, add it to the list of
//					// interceptors to invoke on "after" action
//					// note that we add it to the start, so we can invoke them
//					// in reverse order in "after" action
//					this.invokedInterceptors.add(0, interceptorEntry);
//
//					if (decision == MethodInterceptorManager.this.abortDecision) {
//						return false;
//					} else if (decision != MethodInterceptorManager.this.continueDecision) {
//						throw new IllegalStateException(
//								"Method interceptor's \"before\" did not use MethodInvocation.continue/abort methods");
//					}
//				} catch (Throwable throwable) {
//					this.throwable = throwable;
//					this.returnValue = null;
//					return false;
//				}
//			}
//			return true;
//		}
//
//		private Object afterInvocation() throws Throwable {
//			for (InterestedMethodInterceptorEntry interceptorEntry : this.invokedInterceptors) {
//				MethodInterceptor interceptor = interceptorEntry.target;
//				try {
//					if (this.throwable != null) {
//						AfterMethodExceptionImpl invocation = getAfterThrowable();
//						invocation.methodInterceptorEntry = interceptorEntry;
//						this.returnValue = interceptor.afterThrowable(invocation);
//					} else {
//						AfterMethodInvocationImpl invocation = getAfterInvocation();
//						invocation.methodInterceptorEntry = interceptorEntry;
//						this.returnValue = interceptor.afterInvocation(invocation);
//					}
//					this.throwable = null;
//				} catch (Throwable throwable) {
//					this.throwable = throwable;
//					this.returnValue = null;
//				}
//			}
//
//			if (this.throwable != null) {
//				throw this.throwable;
//			} else {
//				return this.returnValue;
//			}
//		}
//
//		private BeforeMethodInvocationImpl getBeforeInvocation() {
//			if (this.beforeInvocation == null) {
//				this.beforeInvocation = new BeforeMethodInvocationImpl(this);
//			}
//			return this.beforeInvocation;
//		}
//
//		private AfterMethodInvocationImpl getAfterInvocation() {
//			if (this.afterInvocation == null) {
//				this.afterInvocation = new AfterMethodInvocationImpl(this);
//			}
//			return this.afterInvocation;
//		}
//
//		private AfterMethodExceptionImpl getAfterThrowable() {
//			if (this.afterThrowable == null) {
//				this.afterThrowable = new AfterMethodExceptionImpl(this);
//			}
//			return this.afterThrowable;
//		}
//
//		private Map<String, Object> getInterceptorInvocationContext(InterestedMethodInterceptorEntry interceptorEntry) {
//			if (this.interceptorInvocationContexts == null) {
//				this.interceptorInvocationContexts = new HashMap<>();
//			}
//
//			Map<String, Object> context = this.interceptorInvocationContexts.get(interceptorEntry);
//			if (context == null) {
//				context = new HashMap<>();
//				this.interceptorInvocationContexts.put(interceptorEntry, context);
//			}
//			return context;
//		}
//	}
//
//	private class ContinueDecision implements MethodInterceptor.BeforeInvocationDecision {
//	}
//
//	private class AbortDecision implements MethodInterceptor.BeforeInvocationDecision {
//	}
//
//	private abstract class AbstractMethodInvocation implements MethodInterceptor.MethodInvocation {
//
//		protected final InvocationContext context;
//
//		protected InterestedMethodInterceptorEntry methodInterceptorEntry;
//
//		protected AbstractMethodInvocation(InvocationContext context) {
//			this.context = context;
//		}
//
//		@Override
//		public final Map<String, Object> getInterceptorContext() {
//			if (this.methodInterceptorEntry == null) {
//				throw new IllegalStateException("Interceptor context not set on invocation!");
//			}
//			return this.methodInterceptorEntry.interceptorContext;
//		}
//
//		@Override
//		public final Map<String, Object> getInvocationContext() {
//			if (this.methodInterceptorEntry == null) {
//				throw new IllegalStateException("Interceptor context not set on invocation!");
//			}
//			return this.context.getInterceptorInvocationContext(this.methodInterceptorEntry);
//		}
//
//		@Override
//		public final Method getMethod() {
//			return this.context.methodEntry.getMethod();
//		}
//
//		@Override
//		public final Object getObject() {
//			return this.context.object;
//		}
//	}
//
//	private class BeforeMethodInvocationImpl extends AbstractMethodInvocation
//			implements MethodInterceptor.BeforeMethodInvocation {
//		public BeforeMethodInvocationImpl(InvocationContext context) {
//			super(context);
//		}
//
//		@Override
//		public Object[] getArguments() {
//			return this.context.arguments;
//		}
//
//		@Override
//		public MethodInterceptor.BeforeInvocationDecision continueInvocation() {
//			return MethodInterceptorManager.this.continueDecision;
//		}
//
//		@Override
//		public MethodInterceptor.BeforeInvocationDecision abort(Object returnValue) {
//			this.context.returnValue = returnValue;
//			return MethodInterceptorManager.this.abortDecision;
//		}
//	}
//
//	private class AfterMethodInvocationImpl extends AbstractMethodInvocation
//			implements MethodInterceptor.AfterMethodInvocation {
//
//		private final Object[] arguments;
//
//		public AfterMethodInvocationImpl(InvocationContext context) {
//			super(context);
//			this.arguments = copyOf(this.context.arguments, this.context.arguments.length);
//		}
//
//		@Override
//		public Object[] getArguments() {
//			return this.arguments;
//		}
//
//		@Override
//		public Object getReturnValue() {
//			return this.context.returnValue;
//		}
//	}
//
//	private class AfterMethodExceptionImpl extends AbstractMethodInvocation
//			implements MethodInterceptor.AfterMethodException {
//
//		private final Object[] arguments;
//
//		public AfterMethodExceptionImpl(InvocationContext context) {
//			super(context);
//			this.arguments = copyOf(this.context.arguments, this.context.arguments.length);
//		}
//
//		@Override
//		public Object[] getArguments() {
//			return this.arguments;
//		}
//
//		@Override
//		public Throwable getThrowable() {
//			Throwable throwable = this.context.throwable;
//			if (throwable == null) {
//				throw new IllegalStateException("No throwable found");
//			} else {
//				return throwable;
//			}
//		}
//	}
//}
