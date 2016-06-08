package io.skysail.server.um.shiro.repository;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.skysail.server.um.domain.SkysailRole;
import io.skysail.server.um.domain.SkysailUser;
import lombok.extern.slf4j.Slf4j;

/**
 * A repository for users, passwords and roles, created in memory and
 * initialized with data from the configuration admin, that is, from a
 * configuration file.
 *
 */
@Slf4j
public class UserManagementRepository {

    private volatile Map<String, SkysailUser> users = new ConcurrentHashMap<>();

    public UserManagementRepository(Map<String, String> config) {
        String usersDefinition = config.get("users");
        if (usersDefinition == null || usersDefinition.length() == 0) {
            log.warn("no users are defined");
            return;
        }
        createUsers(usersDefinition, config);
    }

    public SkysailUser getByPrincipal(String principal) {
        return users.get(principal);
    }

    public SkysailUser getByUsername(String username) {
        Optional<SkysailUser> optionalUser = users.values().stream().filter(u ->
            u.getUsername().equals(username)
        ).findFirst();
        return optionalUser.orElse(null);
    }

    private void createUsers(String usersDefinition, Map<String, String> config) {
        Arrays.stream(usersDefinition.split(",")).map(String::trim).forEach(setUserAndRoles(config)); // NOSONAR
    }

    private Consumer<? super String> setUserAndRoles(Map<String, String> config) {
        return username -> createUserWithRoles(config, username);
    }

    private void createUserWithRoles(Map<String, String> config, String username) {
        String password = config.get(username + ".password");
        String id = config.get(username + ".id");
        SkysailUser simpleUser = addToUsersMap(username, password, id);

        if (config.get(username + ".email") != null) {
            simpleUser.setEmail(config.get(username + ".email"));
        }

        if (config.get(username + ".name") != null) {
            simpleUser.setEmail(config.get(username + ".name"));
        }

        if (config.get(username + ".surname") != null) {
            simpleUser.setEmail(config.get(username + ".surname"));
        }

        String rolesDefinition = config.get(username + ".roles");
        if (rolesDefinition != null && simpleUser != null) {
            Set<SkysailRole> roles = Arrays.stream(rolesDefinition.split(",")).map(r -> { // NOSONAR
                return new SkysailRole(r.trim());
            }).collect(Collectors.toSet());
            simpleUser.setRoles(roles);
        }
    }

    private SkysailUser addToUsersMap(String username, String password, String id) {
        if (id == null) {
            throw new IllegalStateException("could not find ID for user '" + username + "'");
        }
        SkysailUser simpleUser = null;
        if (password != null && id != null) {
            simpleUser = new SkysailUser(username, password,id);
            if (users.get(id) != null) {
                throw new IllegalStateException("user with ID '" + id + "' is already defined.");
            }
            users.put(id, simpleUser);
        } else {
            throw new IllegalStateException("trying to define user '" + username + "' without id and/or password");
        }
        return simpleUser;
    }

}
