package io.skysail.server.app.resources;

import java.util.Set;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Data;

@Data
public class UserProfile implements Identifiable {

    private String id;

    @Field
    private String username;

    @Field
    private Set<String> roles;
}
