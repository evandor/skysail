package io.skysail.domain;

import java.util.Date;

import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class GenericIdentifiable implements Entity {

    @Setter
    private String id;

    @Field
    private String payload;

    public GenericIdentifiable() {
        this(null);
    }

    public GenericIdentifiable(Object payload) {
        this.payload = payload == null ? null : payload.toString();
        this.id = Long.toString(new Date().getTime());
    }

}
