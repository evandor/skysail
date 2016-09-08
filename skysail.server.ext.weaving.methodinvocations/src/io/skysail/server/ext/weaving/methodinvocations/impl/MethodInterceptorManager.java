package io.skysail.server.ext.weaving.methodinvocations.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

import org.osgi.service.component.annotations.Component;

import io.skysail.server.ext.weaving.methodinvocations.MethodEntry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MethodInterceptorManager implements MethodInvocationsCountDataProvider {

    private static final ConcurrentHashMap<String, LongAdder> counter = new ConcurrentHashMap<>();

    @Override
    public Map<String, Long> getMethodInvocations() {
        Map<String, Long> result = new HashMap<>();
        counter.entrySet().forEach(e -> {
            result.put(e.getKey(), e.getValue().sum());
        });
        return result;
    }

    public boolean beforeInvocation(MethodEntry methodEntry, Object object, Object[] arguments) throws Throwable {
        // System.out.println("before:" + methodEntry);
        String ident = methodEntry.getModuleId() + "-" + methodEntry.getId() + "-" + methodEntry.getMethod();
        counter.computeIfAbsent(ident, k -> new LongAdder()).increment();
        return true;
    }

    public void cleanup(MethodEntry methodEntry) {
    }

    private class InterestedMethodInterceptorEntry {

        private final MethodInterceptor target;

        private final Map<String, Object> interceptorContext;

        private InterestedMethodInterceptorEntry(MethodInterceptor target, Map<String, Object> interceptorContext) {
            this.target = target;
            this.interceptorContext = interceptorContext;
        }
    }

//    private class InvocationContext extends HashMap<String, Object> {
//
//        private final MethodEntry methodEntry;
//
//        private final Object object;
//
//        private final Object[] arguments;
//
//        private final List<InterestedMethodInterceptorEntry> invokedInterceptors = new LinkedList<>();
//
//        private Map<InterestedMethodInterceptorEntry, Map<String, Object>> interceptorInvocationContexts = null;
//
//        private Throwable throwable;
//
//        private Object returnValue;
//
//        private InvocationContext(MethodEntry methodEntry, Object object, Object[] arguments)
//                throws ClassNotFoundException {
//            super(5);
//            this.methodEntry = methodEntry;
//            this.object = object;
//            this.arguments = arguments;
//        }
//
//    }

}
