package io.skysail.server.app.plugins.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.plugins.Bookmark;
import io.skysail.server.app.plugins.PluginsApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutBookmarkResource extends PutEntityServerResource<Bookmark> {

    protected String id;
    protected PluginsApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (PluginsApplication)getApplication();
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