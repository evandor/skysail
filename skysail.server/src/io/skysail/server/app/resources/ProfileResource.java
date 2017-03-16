package io.skysail.server.app.resources;

import java.util.HashSet;
import java.util.Set;

import io.skysail.server.restlet.resources.EntityServerResource;

public class ProfileResource extends EntityServerResource<UserProfile> {

    @Override
    public UserProfile getEntity() {
        UserProfile userProfile = new UserProfile();
        userProfile.setId("admin");
        userProfile.setUsername("admin");
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        roles.add("user");
        userProfile.setRoles(roles);
        return userProfile;
    }

}
