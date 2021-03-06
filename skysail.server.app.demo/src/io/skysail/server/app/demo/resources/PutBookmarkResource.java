package io.skysail.server.app.demo.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.demo.Bookmark;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutBookmarkResource extends PutEntityServerResource<Bookmark> {

    protected String id;
    protected DemoApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (DemoApplication)getApplication();
    }

    @Override
    public void updateEntity(Bookmark  entity) {
        Bookmark original = getEntity();
        copyProperties(original,entity);

        app.getRepo().update(original,app.getApplicationModel());
    }

    @Override
    public Bookmark getEntity() {
        return app.getRepo().findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(BookmarksResource.class);
    }
}