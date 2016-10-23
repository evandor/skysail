package io.skysail.api.metrics.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.skysail.api.metrics.CounterMetric;

public class CounterMetricTest {

    @Test
    public void euqals_on_class_and_identifier() {
        CounterMetric counterMetric1 = new CounterMetric(String.class, "ident");
        CounterMetric counterMetric2 = new CounterMetric(String.class, "ident");
        assertThat(counterMetric1,is(counterMetric2));
    }

    @Test
    public void euqals_on_class_and_identifier_2() {
        CounterMetric counterMetric1 = new CounterMetric(String.class, "ident1");
        CounterMetric counterMetric2 = new CounterMetric(String.class, "ident2");
        assertThat(counterMetric1,is(not(counterMetric2)));
    }

    @Test
    public void euqals_on_class_and_identifier_3() {
        CounterMetric counterMetric1 = new CounterMetric(String.class, "ident");
        CounterMetric counterMetric2 = new CounterMetric(Integer.class, "ident");
        assertThat(counterMetric1,is(not(counterMetric2)));
    }

}
