package io.skysail.server.app.ref.fields;

import javax.annotation.Generated;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.PutEntityServerResource;

@Generated("io.skysail.server.codegen.apt.processors.EntityProcessor")
public class PutEntityWithoutTabsResource extends PutEntityServerResource<EntityWithoutTabs> {

    protected FieldsDemoApplication app;
    protected EntityWithoutTabsRepo repository;

	protected void doInit() {
        super.doInit();
        app = (FieldsDemoApplication) getApplication();
        //repository = (EntityWithoutTabsRepo) app.getRepository(EntityWithoutTabs.class);
    }

    public EntityWithoutTabs getEntity() {
        return repository.findOne(getAttribute("id"));
    }

    public void updateEntity(EntityWithoutTabs entity) {
        repository.update(entity, app.getApplicationModel());
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(EntityWithoutTabssResource.class);
    }
}
