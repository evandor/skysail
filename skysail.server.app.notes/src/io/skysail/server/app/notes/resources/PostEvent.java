package io.skysail.server.app.notes.resources;

import io.skysail.domain.Identifiable;
import io.skysail.server.app.notes.Event;
import io.skysail.server.app.notes.Note;

public class PostEvent extends Event implements Identifiable {
	
	public PostEvent(Note note) {
		this.setType(EventType.CREATE);
	}

}
