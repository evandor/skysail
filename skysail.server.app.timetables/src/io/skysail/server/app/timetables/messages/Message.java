package io.skysail.server.app.timetables.messages;

import io.skysail.domain.Identifiable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message implements Identifiable {

	private String id;
}
