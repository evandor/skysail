package io.skysail.api.metrics;

public interface TimerDataProvider {

	public String getName();
	public long getCount();
	public double getMax();
	public double getMean();
	public double getMin();

}
