package io.skysail.server.app.plugins.features;

import java.util.List;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.plugins.PluginsApplication;
import io.skysail.server.restlet.resources.ListServerResource;

public class FeaturesResource extends ListServerResource<Feature> {

    private PluginsApplication app;

    public FeaturesResource() {
        super(FeatureResource.class);
        app = (PluginsApplication) getApplication();
        addToContext(ResourceContextId.LINK_TITLE, "Features");
    }

//    @Override
//    public List<Link> getLinks() {
//        return super.getLinks(PluginRootResource.class);
//    }

    @Override
    public List<Feature> getEntity() {
        return null;
    }

}
