package io.skysail.domain.core.test;

import io.skysail.domain.Entity;
import io.skysail.domain.html.*;
import lombok.*;

@Getter
@Setter
public class AThing implements Entity {

    private String id;

    @Field(inputType = InputType.TEXT)
    private String stringField;
}