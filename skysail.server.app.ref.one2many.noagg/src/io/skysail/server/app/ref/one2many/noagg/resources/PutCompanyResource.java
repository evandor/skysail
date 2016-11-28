package io.skysail.server.app.ref.one2many.noagg.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.ref.one2many.noagg.One2ManyNoAggApplication;
import io.skysail.server.app.ref.one2many.noagg.Company;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutCompanyResource extends PutEntityServerResource<Company> {

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

        app.getRepo().update(original,app.getApplicationModel());
    }

    @Override
    public Company getEntity() {
        return app.getRepo().findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(CompaniesResource.class);
    }
}
