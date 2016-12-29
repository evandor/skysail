package io.skysail.server.app.ref.fields;

import javax.annotation.Generated;

// test

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.ResourceContextId;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PostEntityWithoutTabsResource extends PostEntityServerResource<EntityWithoutTabs> {

	private FieldsDemoApplication app;
    private EntityWithoutTabsRepo repository;

	public PostEntityWithoutTabsResource() {
	    addToContext(ResourceContextId.LINK_TITLE, "Create new EntityWithoutTabs");
    }

    @Override
	protected void doInit() {
		app = (FieldsDemoApplication)getApplication();
        //repository = (EntityWithoutTabsRepo) app.getRepository(EntityWithoutTabs.class);
	}

	@Override
    public EntityWithoutTabs createEntityTemplate() {
	    return new EntityWithoutTabs();
    }

    @Override
    public void addEntity(EntityWithoutTabs entity) {
        String id = repository.save(entity, app.getApplicationModel()).toString();
        entity.setId(id);
    }

	@Override
	public String redirectTo() {
	    return super.redirectTo(EntityWithoutTabssResource.class);
	}
}