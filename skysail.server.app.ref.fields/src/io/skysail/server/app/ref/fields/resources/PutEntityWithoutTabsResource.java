package io.skysail.server.app.ref.fields.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.ref.fields.EntityWithoutTabs;
import io.skysail.server.app.ref.fields.FieldsDemoApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutEntityWithoutTabsResource extends PutEntityServerResource<EntityWithoutTabs> {

    protected String id;
    protected FieldsDemoApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (FieldsDemoApplication)getApplication();
    }

    @Override
    public void updateEntity(EntityWithoutTabs  entity) {
    	EntityWithoutTabs original = getEntity();
        copyProperties(original,entity);

        app.getRepo().update(original,app.getApplicationModel());
    }

    @Override
    public EntityWithoutTabs getEntity() {
        return app.getEntitiesWoTabsRepo().findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(EntitiesWithoutTabsResource.class);
    }
}