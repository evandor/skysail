package io.skysail.server.um.repository.filebased;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.restlet.engine.util.StringUtils;
import org.restlet.security.Role;
import org.restlet.security.User;

import lombok.extern.slf4j.Slf4j;

/**
 * A repository for users, passwords and roles, created in memory and
 * initialized with data from the configuration admin, that is, from a
 * configuration file.
 *
 */
@Slf4j
@Component
public class FilebasedUserManagementRepository {

    private Map<String, User> users = new HashMap<>();
    private Set<Role> roles = new HashSet<>();
    private Map<User,Set<Role>> usersRoles = new HashMap<>();

    @Activate
    public synchronized void activate(Map<String, String> config) {
        String usersDefinition = config.get("users");
        if (StringUtils.isNullOrEmpty(usersDefinition)) {
            log.warn("no users are defined");
            return;
        }
        createUsers(usersDefinition, config);
    }

    @Deactivate
    public void deactivate() {
        users = new HashMap<>();
        roles = new HashSet<>();
    }

    public Map<String, User> getUsers() {
        return Collections.unmodifiableMap(users);
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public Map<User, Set<Role>> getUsersRoles() {
        return Collections.unmodifiableMap(usersRoles);
    }

    private void createUsers(String usersDefinition, Map<String, String> config) {
        Arrays.stream(usersDefinition.split(",")).map(String::trim).forEach(setUserAndRoles(config)); // NOSONAR
    }

    private Consumer<? super String> setUserAndRoles(Map<String, String> config) {
        return username -> { // NOSONAR
            String password = config.get(username + ".password");
            String id = config.get(username + ".id");
            User restletUser = createUser(username, password, id);
            String name = config.get(username + ".name");
            if (name != null) {
                restletUser.setFirstName(name);
            }
            String surname = config.get(username + ".surname");
            if (surname != null) {
                restletUser.setLastName(surname);
            }
            String email = config.get(username + ".email");
            if (email != null) {
                restletUser.setEmail(email);
            }
            String rolesDefinition = config.get(username + ".roles");
            if (rolesDefinition != null && restletUser != null) {
                Set<Role> rolesFromDefinition = Arrays.stream(rolesDefinition.split(",")).map(r -> new Role(r.trim())).collect(Collectors.toSet());
                roles.addAll(rolesFromDefinition);
            }
            users.put(username, restletUser);
            if (rolesDefinition != null) {
                Arrays.stream(rolesDefinition.split(",")).map(String::trim).forEach(rolename -> connectUserWithRole(restletUser, rolename));
            }
        };
    }

    private void connectUserWithRole(User restletUser, String rolename) {
        @SuppressWarnings("deprecation")
        Consumer<? super Role> consumer = role -> {
           Set<Role> userRoles = usersRoles.get(restletUser.getIdentifier());
           if (userRoles == null) {
               userRoles = new HashSet<>();
               usersRoles.put(restletUser, userRoles);
           }
           userRoles.add(new Role(rolename));
        };
        roles.stream().filter(r -> rolename.equals(r.getName())).findFirst().ifPresent(consumer);
    }

    private User createUser(String username, String password, String id) {
        if (id == null) {
            throw new IllegalStateException("could not find ID for user '" + username + "'");
        }
        User simpleUser;
        if (password != null) {
            simpleUser = new User(username, password);
            if (users.get(id) != null) {
                throw new IllegalStateException("user with ID '" + id + "' is already defined.");
            }
        } else {
            throw new IllegalStateException("trying to define user '" + username + "' without id and/or password");
        }
        return simpleUser;
    }

}
