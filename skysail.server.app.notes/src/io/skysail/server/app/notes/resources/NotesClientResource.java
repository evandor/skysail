package io.skysail.server.app.notes.resources;

import io.skysail.server.ResourceContextId;

public class NotesClientResource extends NotesResource {
	
	public NotesClientResource() {
		super();
        addToContext(ResourceContextId.RENDERER_HINT, "semanticui");
    }

}