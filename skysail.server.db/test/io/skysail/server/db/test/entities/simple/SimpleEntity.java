package io.skysail.server.db.test.entities.simple;

import io.skysail.domain.Entity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SimpleEntity implements Entity {

    private String id;

    private String name;

    public SimpleEntity(String name) {
        this.name = name;
    }
}
