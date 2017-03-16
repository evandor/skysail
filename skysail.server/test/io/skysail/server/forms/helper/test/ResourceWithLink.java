package io.skysail.server.forms.helper.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.ListServerResource;

public class ResourceWithLink extends ListServerResource<Entity> {

    @Override
    public List<Entity> getEntity() {
        return Arrays.asList(new Entity() {
            @Override
            public String getId() {
                return "theId";
            }});
    }

    @Override
    public List<Link> getLinks() {
        List<Link> links = new ArrayList<>();
        links.add(new Link.Builder("uri").build());
        return links;
    }

}
