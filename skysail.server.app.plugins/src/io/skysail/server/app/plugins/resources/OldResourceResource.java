package io.skysail.server.app.plugins.resources;

import java.util.List;

import org.restlet.resource.ResourceException;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.plugins.PluginsApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

public class OldResourceResource extends EntityServerResource<Resource> {

    private String symbolicName;
    private String version;
    private PluginsApplication app;

    public OldResourceResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Details");
    }

    @Override
    protected void doInit() throws ResourceException {
        String identifier = getAttribute("id");
        if (identifier == null) {
            return;
        }
        String[] split = identifier.split(";");
        symbolicName = split[0];
        version = split[1];
        app = (PluginsApplication) getApplication();
    }

    // @Override
    // public Resource getData() {
    // // TODO user input! sanitized???
    // String searchTerm = "(&(symbolicname=" + symbolicName + ")(version=" +
    // version + "))";
    // List<org.apache.felix.bundlerepository.Resource> discoverdResources =
    // app.discoverResources(searchTerm);
    // if (discoverdResources.size() != 1) {
    // throw new
    // IllegalStateException("expected exactly one bundle for this search");
    // }
    // return new Resource(discoverdResources.get(0),
    // Arrays.asList(app.getBundleContext().getBundles()));
    // }

    @Override
    public String getId() {
        return "7";
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(OldResourceResource.class);
    }

    @Override
    public Resource getEntity() {
        return null;
    }
}
