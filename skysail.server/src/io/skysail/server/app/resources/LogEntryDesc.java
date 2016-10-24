package io.skysail.server.app.resources;

import org.osgi.service.log.LogEntry;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogEntryDesc implements Identifiable {

	private String id;
	
	@Field
	private long time;
	@Field
	private int level;
	@Field
	private String message;

	public LogEntryDesc(LogEntry log) {
		time = log.getTime();
		level = log.getLevel();
		message = log.getMessage();
	}

}
