package io.skysail.server.domain.jvm.test;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import lombok.*;

@Getter
@Setter
public class AThing implements Entity {

    private String id;

    @Field
    private String stringField;
}