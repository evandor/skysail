package io.skysail.server.app.reference.one2manyNoAgg.resources;

import io.skysail.server.app.reference.one2manyNoAgg.One2ManyNoAggApplication;
import io.skysail.server.app.reference.one2manyNoAgg.Company;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostTodoListResource extends PostEntityServerResource<Company> {

    private One2ManyNoAggApplication app;

    @Override
    public void doInit() {
        app = (One2ManyNoAggApplication) getApplication();
    }

    @Override
    public Company createEntityTemplate() {
        return new Company();
    }

    @Override
    public void addEntity(Company entity) {
        String id = app.getRepository(Company.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TodoListsResource.class);
    }

}
