package io.skysail.server.app.ref.one2many.noagg.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.ref.one2many.noagg.One2ManyNoAggApplication;
import io.skysail.server.app.ref.one2many.noagg.One2ManyNoAggRepository;
import io.skysail.server.app.ref.one2many.noagg.Contact;
import io.skysail.server.app.ref.one2many.noagg.Company;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;

public class CompanysResource extends ListServerResource<Contact> {

    private One2ManyNoAggApplication app;
    private One2ManyNoAggRepository repository;

    public CompanysResource() {
        super(CompanyResource.class);
    }

    @Override
    protected void doInit() {
        app = (One2ManyNoAggApplication) getApplication();
        repository = (One2ManyNoAggRepository) app.getRepository(Company.class);
    }

    @Override
    public List<?> getEntity() {
       //return repository.find(new Filter(getRequest()));
        String sql = "SELECT from " + DbClassName.of(Contact.class) + " WHERE #" + getAttribute("id") + " IN in('todos')";
        return null;//((SpaceRepository)app.getRepository(Space.class)).execute(Course.class, sql);
    }


    @Override
    public List<Link> getLinks() {
        // this will add a link to the "post new account" resource
        return super.getLinks(PostCompanyResource.class);
    }

}
