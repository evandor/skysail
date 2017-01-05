package io.skysail.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class GenericIdentifiable implements Identifiable {

    @Setter
    private String id;
    private String payload;

    public GenericIdentifiable() {
        this(null);
    }

    public GenericIdentifiable(Object payload) {
        this.payload = payload.toString();
        this.id = Long.toString(new Date().getTime());
    }

}
