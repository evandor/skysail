package io.skysail.server.um.repository.filebased;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.restlet.engine.util.StringUtils;
import org.restlet.security.Role;
import org.restlet.security.User;

import io.skysail.api.um.UserManagementRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * A repository for users, passwords and roles, created in memory and
 * initialized with data from the configuration admin, that is, from a
 * configuration file.
 *
 */
@Slf4j
@Component(immediate = true, configurationPid = "users")
public class FilebasedUserManagementRepository implements UserManagementRepository {

    private Map<String, User> users = new HashMap<>();
    private Set<Role> roles = new HashSet<>();
    private Map<User, Set<Role>> usersRoles = new HashMap<>();

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

    @Override
    public Optional<User> getUser(String username) {
        return Optional.ofNullable(users.get(username));
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

    private void createUsers(String usersDefinition, Map<String, String> config) {
        Arrays.stream(usersDefinition.split(",")).map(String::trim).forEach(setUserAndRoles(config)); // NOSONAR
    }

    private Consumer<? super String> setUserAndRoles(Map<String, String> config) {
        return username -> { // NOSONAR
            String password = config.get(username + ".password");
            String id = config.get(username + ".id");
            Optional<User> restletUser = createUser(username, password, id);
            if (restletUser.isPresent()) {
                String name = config.get(username + ".name");
                if (name != null) {
                    restletUser.get().setFirstName(name);
                }
                String surname = config.get(username + ".surname");
                if (surname != null) {
                    restletUser.get().setLastName(surname);
                }
                String email = config.get(username + ".email");
                if (email != null) {
                    restletUser.get().setEmail(email);
                }
                String rolesDefinition = config.get(username + ".roles");
                if (rolesDefinition != null && restletUser != null) {
                    @SuppressWarnings("deprecation")
                    Set<Role> rolesFromDefinition = Arrays.stream(rolesDefinition.split(","))
                            .map(r -> new Role(r.trim())).collect(Collectors.toSet());
                    roles.addAll(rolesFromDefinition);
                }
                users.put(username, restletUser.get());
                usersRoles.put(restletUser.get(), new HashSet<>());
                if (rolesDefinition != null) {
                    Arrays.stream(rolesDefinition.split(",")).map(String::trim)
                            .forEach(rolename -> connectUserWithRole(restletUser.get(), rolename));
                }
            }
        };
    }

    private void connectUserWithRole(User restletUser, String rolename) {
        @SuppressWarnings("deprecation")
        Consumer<? super Role> consumer = role -> {
            usersRoles.get(restletUser).add(new Role(rolename));
        };
        roles.stream().filter(r -> rolename.equals(r.getName())).findFirst().ifPresent(consumer);
    }

    private Optional<User> createUser(String username, String password, String id) {
        if (id == null) {
            log.warn("could not find ID for user '{}', ignoring", username);
        }
        User simpleUser = null;
        if (password != null) {
            simpleUser = new User(username, password);
            if (users.get(id) != null) {
                log.warn("user with ID '{}' is already defined.", id);
            }
        } else {
            log.warn("trying to define user '{}' without id and/or password", username);
        }
        return Optional.ofNullable(simpleUser);
    }

}
