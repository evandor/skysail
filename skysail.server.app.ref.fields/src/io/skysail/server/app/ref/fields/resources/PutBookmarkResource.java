package io.skysail.server.app.ref.fields.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.ref.fields.EntityWithTabs;
import io.skysail.server.app.ref.fields.FieldsDemoApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutBookmarkResource extends PutEntityServerResource<EntityWithTabs> {

    protected String id;
    protected FieldsDemoApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (FieldsDemoApplication)getApplication();
    }

    @Override
    public void updateEntity(EntityWithTabs  entity) {
        EntityWithTabs original = getEntity();
        copyProperties(original,entity);

        app.getRepo().update(original,app.getApplicationModel());
    }

    @Override
    public EntityWithTabs getEntity() {
        return app.getRepo().findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(BookmarksResource.class);
    }
}