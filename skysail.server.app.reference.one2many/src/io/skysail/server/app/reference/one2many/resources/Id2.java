package io.skysail.server.app.reference.one2many.resources;

import javax.persistence.Id;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Data;

@Data
public class Id2 implements Identifiable {
    @Id
    private String id;
    @Field private String name;
    public Id2(String id) {
        this.id = id;
        this.name = id;
    }
}