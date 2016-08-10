package io.skysail.server.restlet.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;

public class AggregateResource extends EntityServerResource<Aggregate> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Aggregate getEntity() {
        return null;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks();
    }

}
