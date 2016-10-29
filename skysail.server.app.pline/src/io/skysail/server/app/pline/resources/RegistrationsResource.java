package io.skysail.server.app.pline.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.pline.PlineApplication;
import io.skysail.server.app.pline.PlineRepository;
import io.skysail.server.app.pline.Registration;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class RegistrationsResource extends ListServerResource<Registration> {

    private PlineApplication app;
    private PlineRepository repository;

    public RegistrationsResource() {
        super(RegistrationResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Registrations");
    }

    public RegistrationsResource(Class<? extends RegistrationResource> cls) {
        super(cls);
    }

    @Override
    protected void doInit() {
        app = (PlineApplication) getApplication();
        repository = (PlineRepository) app.getRepository(Registration.class);
    }

    @Override
    public List<Registration> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse());
        return repository.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostRegistrationResource.class, RegistrationsResource.class);
    }
}