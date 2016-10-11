package io.skysail.api.metrics;

public interface MetricsCollector {

    void timerFor(Class<?> cls, String identifier);
}
