package io.skysail.server.um.repository.fixed;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.restlet.security.Role;
import org.restlet.security.User;

import io.skysail.api.um.UserManagementRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * A repository for users, passwords and roles, created in memory and
 * initialized with "fixed logic" for testing purposes.
 *
 */
@Slf4j
@Component(immediate = true)
public class FixedUserManagementRepository implements UserManagementRepository {

    private Map<String, User> users = new HashMap<>();
    private Set<Role> roles = new HashSet<>();
    private Map<User, Set<Role>> usersRoles = new HashMap<>();

    @SuppressWarnings("deprecation")
    @Override
    public Optional<User> getUser(String username) {
        if (users.get(username) == null) {
            log.warn("running fixed UserManagement Repository, i.e. creating dummy user for username '{}'", username);
            users.put(username, new User(username, username.toCharArray(), "firstname", "lastname", "some@email.io"));
        }
        Set<Role> fixedRoles = new HashSet<>();

        Role userRole = new Role("user");
        Role roleNamedLikeUsername = new Role(username);

        roles.add(userRole);
        roles.add(roleNamedLikeUsername);

        fixedRoles.add(userRole);
        fixedRoles.add(roleNamedLikeUsername);

        usersRoles.put(users.get(username), fixedRoles);

        return Optional.of(users.get(username));
    }

    @Override
    public Map<String, User> getUsers() {
        return Collections.unmodifiableMap(users);
    }

    @Override
    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    @Override
    public Map<User, Set<Role>> getUsersRoles() {
        return Collections.unmodifiableMap(usersRoles);
    }

}
