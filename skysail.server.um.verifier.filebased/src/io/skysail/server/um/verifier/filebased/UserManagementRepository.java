package io.skysail.server.um.verifier.filebased;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.restlet.engine.util.StringUtils;
import org.restlet.security.Enroler;
import org.restlet.security.MemoryRealm;
import org.restlet.security.Role;
import org.restlet.security.User;

import io.skysail.server.security.user.UserDetailsService;
import lombok.extern.slf4j.Slf4j;

/**
 * A repository for users, passwords and roles, created in memory and
 * initialized with data from the configuration admin, that is, from a
 * configuration file.
 *
 */
@Slf4j
public class UserManagementRepository implements UserDetailsService {

    private volatile Map<String, User> users = new ConcurrentHashMap<>();

    private MemoryRealm realm = new MemoryRealm();

    private Set<Role> usedRoles = new HashSet<>();

    public UserManagementRepository(Map<String, String> config) {
        String usersDefinition = config.get("users");
        if (StringUtils.isNullOrEmpty(usersDefinition)) {
            log.warn("no users are defined");
            return;
        }
        createUsers(usersDefinition, config);
    }

    public User getByPrincipal(String principal) {
        return users.get(principal);
    }

    @Override
    public User loadUserByUsername(String username) {
        Optional<User> optionalUser = users.values().stream().filter(u -> u.getName().equals(username)).findFirst();
        return optionalUser.orElse(null);
    }

    private void createUsers(String usersDefinition, Map<String, String> config) {
        Arrays.stream(usersDefinition.split(",")).map(String::trim).forEach(setUserAndRoles(config));
    }

    private Consumer<? super String> setUserAndRoles(Map<String, String> config) {
        return username -> { // NOSONAR
            String password = config.get(username + ".password");
            String id = config.get(username + ".id");
            User restletUser = addToUsersMap(username, password, id);
            String rolesDefinition = config.get(username + ".roles");
            if (rolesDefinition != null && restletUser != null) {
                Set<Role> roles = Arrays.stream(rolesDefinition.split(",")).map(r -> {
                    return new Role(r.trim());
                }).collect(Collectors.toSet());

                usedRoles.addAll(roles);

                // simpleUser.setRoles(roles);
            }

            realm.getUsers().add(restletUser);
            if (rolesDefinition != null) {
                Arrays.stream(rolesDefinition.split(",")).map(String::trim).forEach(rolename -> {
                    connectUserWithRole(restletUser, rolename);
                });
            }
        };
    }

    private void connectUserWithRole(User restletUser, String rolename) {
        Consumer<? super Role> consumer = new Consumer<Role>() {
            @Override
            public void accept(Role role) {
                realm.map(restletUser, role);
            }
        };
        usedRoles.stream().filter(r -> rolename.equals(r.getName())).findFirst().ifPresent(consumer);
    }

    private User addToUsersMap(String username, String password, String id) {
        if (id == null) {
            throw new IllegalStateException("could not find ID for user '" + username + "'");
        }
        User simpleUser = null;
        if (password != null && id != null) {
            simpleUser = new User(username, password);
            if (users.get(id) != null) {
                throw new IllegalStateException("user with ID '" + id + "' is already defined.");
            }
            users.put(id, simpleUser);
        } else {
            throw new IllegalStateException("trying to define user '" + username + "' without id and/or password");
        }
        return simpleUser;
    }

    @Override
    public Enroler getEnroler() {
        return realm.getEnroler();
    }

}
