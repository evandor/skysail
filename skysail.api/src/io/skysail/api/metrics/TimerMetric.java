package io.skysail.api.metrics;

import java.util.ArrayList;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = {"cls", "identifier"})
public class TimerMetric {
	
	private Class<?> cls;
	
	private String identifier;
	
	private List<Stoppable> stoppables = new ArrayList<>();

	public TimerMetric(Class<?> cls, String identifier) {
		this.cls = cls;
		this.identifier = identifier;
	}
	
	public void stop() {
		stoppables.stream().forEach(Stoppable::stop);
	}

	public void setStoppables(List<Stoppable> stoppables) {
		this.stoppables = stoppables;
		
	}
}
