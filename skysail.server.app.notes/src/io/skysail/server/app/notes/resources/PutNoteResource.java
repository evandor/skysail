package io.skysail.server.app.notes.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.notes.Note;
import io.skysail.server.app.notes.NotesApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutNoteResource extends PutEntityServerResource<Note> {

    protected String id;
    protected NotesApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (NotesApplication)getApplication();
    }

    @Override
    public void updateEntity(Note  entity) {
        Note original = getEntity();
        copyProperties(original,entity);

        app.getRepo().update(original,app.getApplicationModel());
        app.getAwsRepo().update(original,null);
    }

    @Override
    public Note getEntity() {
        return app.getRepo().findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(NotesResource.class);
    }
}