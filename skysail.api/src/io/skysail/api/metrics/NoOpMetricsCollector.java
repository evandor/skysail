package io.skysail.api.metrics;

public class NoOpMetricsCollector implements MetricsCollector {

    @Override
    public TimerMetric timerFor(Class<?> cls, String identifier) {
        return new TimerMetric(this.getClass(), "noop");
    }

    @Override
    public void markMeter(String identifier) {
    }

}
