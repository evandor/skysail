package io.skysail.server.db.test.entities.one2many;

import io.skysail.domain.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ToMany implements Entity {

    private String id;

    private String name;

    public ToMany(String name) {
        this.name = name;
    }
}
