package io.skysail.server.app.notes;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Event implements Identifiable {

	public enum EventType {
		CREATE, UPDATE, DELETE
	}

	@Id
    private String id;

	private String userUuid;

    private EventType type;

    private String content;


}