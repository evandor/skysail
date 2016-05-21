package io.skysail.server.app.resources;

import java.util.Set;

import io.skysail.domain.Identifiable;
import lombok.Data;

@Data
public class UserProfile implements Identifiable {

    private String id;

    private String username;

    private Set<String> roles;
}
