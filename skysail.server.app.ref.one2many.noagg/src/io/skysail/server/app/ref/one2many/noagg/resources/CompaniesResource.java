package io.skysail.server.app.ref.one2many.noagg.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.ref.one2many.noagg.One2ManyNoAggApplication;
import io.skysail.server.app.ref.one2many.noagg.One2ManyNoAggRepository;
import io.skysail.server.app.ref.one2many.noagg.Company;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class CompaniesResource extends ListServerResource<Company> {

    private One2ManyNoAggApplication app;
    private One2ManyNoAggRepository repository;

    public CompaniesResource() {
        super(CompanyResource.class);
    }

    @Override
    protected void doInit() {
        app = (One2ManyNoAggApplication) getApplication();
        repository = (One2ManyNoAggRepository) app.getRepository(Company.class);
    }

    @Override
    public List<Company> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse());
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
        // this will add a link to the "post new account" resource
        return super.getLinks(PostCompanyResource.class);
    }

}
