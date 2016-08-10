package io.skysail.server.restlet.resources;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Data;

@Data
public class AggregateResource implements Identifiable {

    public AggregateResource(Class<?> c) {
        // TODO Auto-generated constructor stub
    }

    @Field
    private String id;

}
