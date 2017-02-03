package io.skysail.server.app.designer;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.domain.Entity;
import io.skysail.server.app.designer.application.resources.ApplicationsResource;
import io.skysail.server.restlet.resources.ListServerResource;

public class RootResource extends ListServerResource<Entity> {

    @Override
    public List<Link> getLinks() {
        return super.getLinks(ApplicationsResource.class);
    }

    @Override
    public List<Entity> getEntity() {
        return null;
    }

}