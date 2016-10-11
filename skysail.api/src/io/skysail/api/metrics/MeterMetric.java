package io.skysail.api.metrics;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = {"identifier"})
public class MeterMetric {
	
	private String identifier;
	
	public MeterMetric(String identifier) {
		this.identifier = identifier;
	}
	
}
