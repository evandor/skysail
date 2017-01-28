package io.skysail.server.db.impl.test.entities;

import java.util.*;

import io.skysail.domain.Entity;
import io.skysail.domain.html.*;
import lombok.*;

@Data
public class SomeUser implements Entity {

    private String id;

    @Field(inputType = InputType.MULTISELECT)
    private List<SomeRole> roles = new ArrayList<>();

    private String username;

    private String nonMandatoryField;

    public SomeUser(@NonNull String username) {
        this.username = username;
    }


}
