package io.skysail.server.metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.api.metrics.MeterMetric;
import io.skysail.api.metrics.MetricsCollector;
import io.skysail.api.metrics.MetricsImplementation;
import io.skysail.api.metrics.TimerMetric;

@Component(immediate = true)
public class MetricsCollectorImpl implements MetricsCollector {

	private List<MetricsImplementation> metricsImpls = new ArrayList<>();

	private List<TimerMetric> timers = new ArrayList<>();
	private List<MeterMetric> meters = new ArrayList<>();

	@Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
	public void addMetricsImplementation(MetricsImplementation mi) {
		metricsImpls.add(mi);
	}

	public void removeMetricsImplementation(MetricsImplementation mi) {
		metricsImpls.remove(mi);
	}

	@Override
	public TimerMetric timerFor(Class<?> cls, String identifier) {
		TimerMetric metric = new TimerMetric(cls, identifier);
		int indexOf = timers.indexOf(metric);
		if (indexOf < 0) {
			timers.add(metric);
			metricsImpls.stream().forEach(m -> m.registerTimer(metric));
		}

		indexOf = timers.indexOf(metric);
		TimerMetric timerMetric = timers.get(indexOf);
		metric.setStoppables(metricsImpls.stream().map(m -> m.time(timerMetric)).collect(Collectors.toList()));
		return metric;
	}

	@Override
	public void markMeter(String identifier) {
		MeterMetric metric = new MeterMetric(identifier);
		int indexOf = meters.indexOf(metric);
		if (indexOf < 0) {
			meters.add(metric);
			metricsImpls.stream().forEach(m -> m.registerMeter(metric));
		}

		indexOf = meters.indexOf(metric);
		MeterMetric meterMetric = meters.get(indexOf);
		metricsImpls.stream().forEach(m -> m.meter(meterMetric));
	}

}
