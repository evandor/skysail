package io.skysail.api.metrics;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = {"cls", "identifier"})
public class HistogramMetric {
	
	private Class<?> cls;
	
	private String identifier;
	
	public HistogramMetric(Class<?> cls, String identifier) {
		this.cls = cls;
		this.identifier = identifier;
	}
	
}
