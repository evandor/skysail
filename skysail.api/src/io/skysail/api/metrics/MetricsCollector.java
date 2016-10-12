package io.skysail.api.metrics;

public interface MetricsCollector {

    TimerMetric timerFor(Class<?> cls, String identifier);

    void markMeter(String identifier);
}
