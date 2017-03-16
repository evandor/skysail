package io.skysail.server.restlet.resources;

import java.util.List;

import io.skysail.api.links.Link;

public class AggregateResource extends EntityServerResource<Aggregate> {

    @Override
    public Aggregate getEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks();
    }

}
