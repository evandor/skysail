package io.skysail.server.metrics;

import java.util.ArrayList;
import java.util.List;

import io.skysail.api.metrics.MetricsCollector;
import lombok.Getter;

public class MetricsCollectorImpl implements MetricsCollector {

    @Getter
    public class Timer {

        private Class<?> cls;
        private String identifier;

        public Timer(Class<?> cls, String identifier) {
            this.cls = cls;
            this.identifier = identifier;
        }

    }

    private List<Timer> timers = new ArrayList<>();

    @Override
    public void timerFor(Class<?> cls, String identifier) {
        timers.add(new Timer(cls, identifier));
    }


}
