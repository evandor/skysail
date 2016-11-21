package io.skysail.server.app.plugins.obr;

import java.util.List;

import org.restlet.resource.ResourceException;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.plugins.PluginsApplication;
import io.skysail.server.app.plugins.resources.ResourcesResource;
import io.skysail.server.restlet.resources.ListServerResource;

public class RepositoryResource extends ListServerResource<ObrResource> {

    private PluginsApplication app;
    private String id;

    public RepositoryResource() {
        app = (PluginsApplication) getApplication();
    }

    @Override
    protected void doInit() throws ResourceException {
        id = (String) getRequest().getAttributes().get("id");
    }

    @Override
    public SkysailResponse<ObrResource> eraseEntity() {
        return null;
    }

    @Override
    public List<ObrResource> getEntity() {
         ObrRepository repository = app.getReposList().stream().filter(r -> r.getId().equals(id)).findFirst().orElse(null);
         return repository.getResources();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(RepositoryResource.class, ResourcesResource.class);
    }

}
