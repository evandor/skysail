package io.skysail.api.metrics;

import java.util.List;

public interface MetricsImplementation {

	void registerTimer(TimerMetric metric);

	Stoppable time(TimerMetric timerMetric);


	void registerMeter(MeterMetric metric);

	void meter(MeterMetric identifier);

	
	void registerCounter(CounterMetric metric);

	void inc(CounterMetric metric);
	
	void dec(CounterMetric metric);

	
	void registerHistogram(HistogramMetric metric);

	void update(HistogramMetric metric, long value);
	
	List<TimerDataProvider> getTimers();

}
