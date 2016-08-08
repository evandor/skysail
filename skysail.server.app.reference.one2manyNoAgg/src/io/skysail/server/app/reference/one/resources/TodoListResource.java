package io.skysail.server.app.reference.one.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.reference.one.Company;
import io.skysail.server.app.reference.one.One2ManyNoAggApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

public class TodoListResource extends EntityServerResource<Company> {

    private String id;
    private One2ManyNoAggApplication app;

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (One2ManyNoAggApplication) getApplication();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutTodoListResource.class,PostTodoListToNewTodoRelationResource.class, TodoListsTodosResource.class);
    }

    @Override
    public Company getEntity() {
        return (Company) app.getRepository().findOne(id);
    }

}
