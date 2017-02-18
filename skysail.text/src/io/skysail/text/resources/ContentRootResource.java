package io.skysail.text.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.ListServerResource;

public class ContentRootResource extends ListServerResource<Entity> {

    @Override
    public List<Link> getLinks() {
        return super.getLinks(MessagesResource.class);
    }

    @Override
    public List<Entity> getEntity() {
        return null;
    }

}
