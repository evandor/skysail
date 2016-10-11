package io.skysail.server.metrics.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import io.skysail.api.metrics.TimerMetric;


public class TimerMetricTest {

	private TimerMetric timerMetric1;
	private TimerMetric timerMetric2;

	@Before
	public void setup() {
		timerMetric1 = new TimerMetric(this.getClass(),"test1");
		timerMetric2 = new TimerMetric(this.getClass(),"test2");
	}

	@Test
	public void attributes_are_set_in_constructor() {
		assertThat(timerMetric1.getCls().getName(),is(this.getClass().getName()));
	}
	
	@Test
	public void metrics_are_not_equal_for_different_names() {
		assertThat(timerMetric1,is(not(timerMetric2)));
	}

	@Test
	public void metrics_are_equal_for_same_class_and_name() {
		assertThat(timerMetric1,is(new TimerMetric(this.getClass(),"test1")));
	}

}
