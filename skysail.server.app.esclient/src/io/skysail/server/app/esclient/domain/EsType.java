package io.skysail.server.app.esclient.domain;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EsType implements Entity {

    @Field
    private String id;

    @Field
    private String name;

    public EsType(String id) {
        this.id = id;
        this.name = id;
    }
}
