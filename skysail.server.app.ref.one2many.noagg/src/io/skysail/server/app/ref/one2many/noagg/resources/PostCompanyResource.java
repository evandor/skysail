package io.skysail.server.app.ref.one2many.noagg.resources;

import io.skysail.server.app.ref.one2many.noagg.One2ManyNoAggApplication;
import io.skysail.server.app.ref.one2many.noagg.Company;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostCompanyResource extends PostEntityServerResource<Company> {

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
        return super.redirectTo(CompaniesResource.class);
    }

}
