package io.skysail.server.app.resources;

import java.util.Set;

import io.skysail.domain.Entity;
import io.skysail.domain.html.Field;
import lombok.Data;

@Data
public class UserProfile implements Entity {

    private String id;

    @Field
    private String username;

    @Field
    private Set<String> roles;
}
