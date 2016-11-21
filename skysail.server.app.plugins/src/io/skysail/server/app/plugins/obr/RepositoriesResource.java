package io.skysail.server.app.plugins.obr;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.plugins.PluginsApplication;
import io.skysail.server.app.plugins.features.FeaturesResource;
import io.skysail.server.app.plugins.resources.ResourcesResource;
import io.skysail.server.restlet.resources.ListServerResource;

public class RepositoriesResource extends ListServerResource<ObrRepository> {

    private PluginsApplication app;

    public RepositoriesResource() {
        super(RepositoryResource.class);
        app = (PluginsApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "Repositories");
    }

    @Override
    public List<ObrRepository> getEntity() {
        return app.getReposList();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(FeaturesResource.class, RepositoriesResource.class,ResourcesResource.class);
    }

}
