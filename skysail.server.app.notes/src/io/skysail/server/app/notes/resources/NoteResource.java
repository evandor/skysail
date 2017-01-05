package io.skysail.server.app.notes.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.notes.NotesApplication;
import io.skysail.server.app.notes.domain.Note;
import io.skysail.server.restlet.resources.EntityServerResource;

public class NoteResource extends EntityServerResource<Note> {

    private String id;
    private NotesApplication app;

    public NoteResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (NotesApplication) getApplication();
    }


    @Override
    public SkysailResponse<?> eraseEntity() {
        Note noteToDelete = app.getRepo().findOne(id);
        app.getEventRepo().delete(noteToDelete);
        app.getRepo().delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public Note getEntity() {
        return (Note)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutNoteResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(NotesResource.class);
    }


}