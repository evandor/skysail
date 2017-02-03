package io.skysail.server.app.designer.application.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.ApplicationStatus;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class ApplicationsResource extends ListServerResource<DbApplication> {

    private DesignerApplication app;
    private DesignerRepository repository;

    public ApplicationsResource() {
        super(ApplicationResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Applications");
    }

    @Override
    protected void doInit() {
        app = (DesignerApplication) getApplication();
        repository = app.getRepository();
    }

    @Override
    public List<DbApplication> getEntity() {
//        DesignerApplication app = (DesignerApplication) getApplication();
//        List<DbApplication> apps = app.getRepository().findAll(DbApplication.class);

        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse());
        List<DbApplication> apps = repository.find(filter, pagination);


        apps.stream().forEach(dbApp -> {
            ApplicationStatus status = app.getAppStatus().get(dbApp.getId().replace("#",""));
            dbApp.setStatus(status != null ? status : ApplicationStatus.UNDEFINED);
        });
        return apps;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PostApplicationResource.class, UpdateBundleResource.class);
    }

}
