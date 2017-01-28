package io.skysail.server.text.store.bundleresource.impl;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.ListServerResource;

public class I18nRootResource extends ListServerResource<Entity> {

    @Override
    public List<Link> getLinks() {
        return super.getLinks(MessagesResource.class);
    }

    @Override
    public List<Entity> getEntity() {
        return null;
    }

}
