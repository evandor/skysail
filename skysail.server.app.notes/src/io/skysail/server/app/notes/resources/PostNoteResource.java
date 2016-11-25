package io.skysail.server.app.notes.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.notes.Note;
import io.skysail.server.app.notes.NotesApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
//        try {
//            entity = new Note();
//            entity.setUrl(new URL("http://www.heise.de"));
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
        String id = app.getRepo().save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(NotesResource.class);
    }

}