package io.skysail.server.restlet;

import io.skysail.api.links.LinkRelation;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class TestServerResource extends SkysailServerResource<Entity> {

    public TestServerResource() {
        super();
    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.ITEM;
    }

    @Override
    public Entity getEntity() {
        return null;
    }

}