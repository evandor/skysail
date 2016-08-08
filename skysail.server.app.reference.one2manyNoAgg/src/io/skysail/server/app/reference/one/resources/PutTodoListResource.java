package io.skysail.server.app.reference.one.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.reference.one.Company;
import io.skysail.server.app.reference.one.One2ManyNoAggApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutTodoListResource extends PutEntityServerResource<Company> {

    protected String id;
    protected One2ManyNoAggApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (One2ManyNoAggApplication)getApplication();
    }

    @Override
    public void updateEntity(Company  entity) {
        Company original = getEntity();
        copyProperties(original,entity);

        app.getRepository(Company.class).update(original,app.getApplicationModel());
    }

    @Override
    public Company getEntity() {
        return (Company)app.getRepository(Company.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(TodoListsResource.class);
    }
}
