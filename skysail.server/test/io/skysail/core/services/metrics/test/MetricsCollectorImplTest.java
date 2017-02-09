package io.skysail.core.services.metrics.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import io.skysail.api.metrics.MetricsImplementation;
import io.skysail.api.metrics.TimerMetric;
import io.skysail.core.services.metrics.MetricsCollectorImpl;

public class MetricsCollectorImplTest {

    private MetricsCollectorImpl metricsCollector;

    private MetricsImplementation mi;

    @BeforeClass
    public static void setUpBeforeClass() {
    }

    @Before
    public void setUp() {
        metricsCollector = new MetricsCollectorImpl();
        mi = Mockito.mock(MetricsImplementation.class);
        metricsCollector.addMetricsImplementation(mi);
    }

    @Test
    public void timerFor() {
        TimerMetric metric = metricsCollector.timerFor(MetricsCollectorImplTest.class, "identifier");
        assertThat(metric.getCls().getName(),is(MetricsCollectorImplTest.class.getName()));
        assertThat(metric.getIdentifier(),is("identifier"));
        //Mockito.verify(mi).registerTimer(metric);
    }

    @Test
    public void markMeter() {

    }

}
