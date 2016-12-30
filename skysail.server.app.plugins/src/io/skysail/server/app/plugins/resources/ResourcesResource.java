package io.skysail.server.app.plugins.resources;

import java.util.List;

import org.restlet.resource.ResourceException;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.plugins.PluginsApplication;
import io.skysail.server.app.plugins.obr.ObrResource;
import io.skysail.server.restlet.resources.ListServerResource;

public class ResourcesResource extends ListServerResource<ObrResource> {

    private String id;
    private PluginsApplication app;

    public ResourcesResource() {
        super(ResourceResource.class);
        addToContext(ResourceContextId.LINK_TITLE, "list Resources");
    }

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (PluginsApplication) getApplication();
    }

    @Override
    public List<ObrResource> getEntity() {
        return app.getResources(id);
    }
}
