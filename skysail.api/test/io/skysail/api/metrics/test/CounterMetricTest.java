package io.skysail.api.metrics.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.skysail.api.metrics.CounterMetric;

public class CounterMetricTest {

    @Test
    public void testName() {
        CounterMetric counterMetric1 = new CounterMetric(String.class, "ident");
        CounterMetric counterMetric2 = new CounterMetric(String.class, "ident");
        assertThat(counterMetric1,is(counterMetric2));
    }
}
