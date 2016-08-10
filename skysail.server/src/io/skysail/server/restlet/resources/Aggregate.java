package io.skysail.server.restlet.resources;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Data;

@Data
public class Aggregate implements Identifiable {

    public Aggregate(Class<?> c) {
        this.id = c.toString();
    }

    @Field
    private String id;

}
