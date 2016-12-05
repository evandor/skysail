package io.skysail.server.app.notes.resources;

import java.util.UUID;

import org.restlet.resource.ResourceException;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.notes.Note;
import io.skysail.server.app.notes.NotesApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostNoteResource extends PostEntityServerResource<Note> {

    protected NotesApplication app;

    public PostNoteResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (NotesApplication) getApplication();
    }

    @Override
    public Note createEntityTemplate() {
        return new Note();
    }

    @Override
    public void addEntity(Note entity) {
        entity.setUuid(UUID.randomUUID().toString());
        String id = app.getRepo().save(entity, app.getApplicationModel()).toString();
        entity.setId(id);
        app.getAwsRepo().save(entity,null);
        app.getEventRepo().save(new PostEvent(entity), getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(NotesResource.class);
    }

}