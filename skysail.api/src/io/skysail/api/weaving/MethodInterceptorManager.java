package io.skysail.api.weaving;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

import org.osgi.service.component.annotations.Component;

@Component
public class MethodInterceptorManager implements MethodInvocationsCountDataProvider {

    private static final ConcurrentHashMap<String, LongAdder> counter = new ConcurrentHashMap<>();

    @Override
    public Map<String, Long> getMethodInvocations() {
        Map<String, Long> result = new HashMap<>();
        counter.entrySet().forEach(e -> result.put(e.getKey(), e.getValue().sum()));
        return result;
    }

    @Override
    public void clearData() {
        counter.clear();
    }

    public boolean beforeInvocation(String ident) {
        counter.computeIfAbsent(ident, k -> new LongAdder()).increment();
        return true;
    }

}
