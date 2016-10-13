package io.skysail.server.ext.asciidoctor.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.ext.asciidoctor.Bookmark;
import io.skysail.server.ext.asciidoctor.AsciiDocApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutBookmarkResource extends PutEntityServerResource<Bookmark> {

    protected String id;
    protected AsciiDocApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (AsciiDocApplication)getApplication();
    }

    @Override
    public void updateEntity(Bookmark  entity) {
        Bookmark original = getEntity();
        copyProperties(original,entity);

        app.getRepository(Bookmark.class).update(original,app.getApplicationModel());
    }

    @Override
    public Bookmark getEntity() {
        return (Bookmark)app.getRepository(Bookmark.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(BookmarksResource.class);
    }
}