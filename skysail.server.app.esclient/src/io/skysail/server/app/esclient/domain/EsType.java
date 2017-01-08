package io.skysail.server.app.esclient.domain;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EsType implements Identifiable {

    @Field
    private String id;

    @Field
    private String name;

    public EsType(String id) {
        this.id = id;
        this.name = id;
    }
}
