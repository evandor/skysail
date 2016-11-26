package io.skysail.server.app.notes.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.notes.Note;
import io.skysail.server.app.notes.NotesApplication;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class NotesResource extends ListServerResource<Note> {

    private NotesApplication app;

    public NotesResource() {
        super(NoteResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Notes");
    }

    public NotesResource(Class<? extends NoteResource> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (NotesApplication) getApplication();
    }

    @Override
    public List<Note> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse());
        return app.getRepo().find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostNoteResource.class, NotesResource.class);
    }
}