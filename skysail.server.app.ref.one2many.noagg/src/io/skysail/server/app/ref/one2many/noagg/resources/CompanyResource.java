package io.skysail.server.app.ref.one2many.noagg.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.ref.one2many.noagg.One2ManyNoAggApplication;
import io.skysail.server.app.ref.one2many.noagg.Company;
import io.skysail.server.restlet.resources.EntityServerResource;

public class CompanyResource extends EntityServerResource<Company> {

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
        return super.getLinks(PutCompanyResource.class,PostTodoListToNewTodoRelationResource.class, CompanysContactsResource.class);
    }

    @Override
    public Company getEntity() {
        return (Company) app.getRepository().findOne(id);
    }

}
