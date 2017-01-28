package io.skysail.server.app.notes.resources;

import io.skysail.domain.Entity;
import io.skysail.server.app.notes.Event;
import io.skysail.server.app.notes.Note;

public class PostEvent extends Event implements Entity {
	
	public PostEvent() {
		setType("CREATED");
	}
	
	public PostEvent(Note note) {
		this();
	}

}
