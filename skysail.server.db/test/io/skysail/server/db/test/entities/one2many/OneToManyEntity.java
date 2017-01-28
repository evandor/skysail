package io.skysail.server.db.test.entities.one2many;

import java.util.*;

import io.skysail.domain.Entity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OneToManyEntity implements Entity {

    public OneToManyEntity(String name) {
        this.name = name;
    }

    private String id;

    private String name;

    private List<ToMany> toManies = new ArrayList<>();


}
