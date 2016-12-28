package io.skysail.server.app.ref.fields.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.ref.fields.EntityWithoutTabs;
import io.skysail.server.app.ref.fields.FieldsDemoApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import lombok.extern.slf4j.Slf4j;

public class PostEntityWithoutTabsResource extends PostEntityServerResource<EntityWithoutTabs> {

	protected FieldsDemoApplication app;

    public PostEntityWithoutTabsResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new");
    }

    @Override
    protected void doInit() throws ResourceException {
        app = (FieldsDemoApplication) getApplication();
    }

    @Override
    public EntityWithoutTabs createEntityTemplate() {
        return new EntityWithoutTabs();
    }

    @Override
    public void addEntity(EntityWithoutTabs entity) {
        String id = app.getRepo().save(entity, app.getApplicationModel()).toString();
        entity.setId(id);

    }

    @Override
    public String redirectTo() {
        return super.redirectTo(EntitiesWithoutTabsResource.class);
    }


}