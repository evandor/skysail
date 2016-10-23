package io.skysail.api.metrics;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = { "cls", "identifier" })
public class CounterMetric {

    private Class<?> cls;

    private String identifier;

    public CounterMetric(Class<?> cls, String identifier) {
        this.cls = cls;
        this.identifier = identifier;
    }

}
