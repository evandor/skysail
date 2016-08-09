package io.skysail.server.app.reference.one2one.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.reference.one2one.One2OneApplication;
import io.skysail.server.app.reference.one2one.One2OneRepository;
import io.skysail.server.app.reference.one2one.Master;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class MastersResource extends ListServerResource<Master> {

    private One2OneApplication app;
    private One2OneRepository repository;

    public MastersResource() {
        super(MasterResource.class);
    }

    @Override
    protected void doInit() {
        app = (One2OneApplication) getApplication();
        repository = (One2OneRepository) app.getRepository(Master.class);
    }

    @Override
    public List<Master> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
        // this will add a link to the "post new account" resource
        return super.getLinks(PostMasterResource.class);
    }

}
