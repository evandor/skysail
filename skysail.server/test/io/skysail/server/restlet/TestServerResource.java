package io.skysail.server.restlet;

import io.skysail.api.links.LinkRelation;
import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class TestServerResource extends SkysailServerResource<Identifiable> {

    public TestServerResource() {
        super();
    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.ITEM;
    }

    @Override
    public Identifiable getEntity() {
        return null;
    }

}