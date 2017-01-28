package io.skysail.server.restlet.resources;

import io.skysail.api.links.Link;
import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import lombok.Data;

@Data
public class Aggregate implements Entity {

    @Field
    private String id;

    @Field(inputType = InputType.URL)
    //@ListView(link = AggregateResource.class)
    private String url;

    public Aggregate(Class<?> c, Link link) {
        this.id = c.toString();
        this.url = link.getUri();
    }

}
