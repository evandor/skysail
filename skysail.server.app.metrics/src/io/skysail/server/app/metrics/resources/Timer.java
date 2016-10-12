package io.skysail.server.app.metrics.resources;

import io.skysail.api.metrics.TimerDataProvider;
import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Timer implements Identifiable {

	@Field
	private String id;
	
	@Field
	private long count;

	@Field
	private double mean;

	public Timer(TimerDataProvider t) {
		id = t.getName();
		count = t.getCount();
		mean = t.getMean();
	}

}
