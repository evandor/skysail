package io.skysail.server.um.repository.filebased.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.restlet.security.Role;
import org.restlet.security.User;

import io.skysail.server.um.repository.filebased.FilebasedUserManagementRepository;

public class FilebasedUserManagementRepositoryTest {

    private FilebasedUserManagementRepository repository;
    private Map<String, String> config;

    @Before
    public void setup() {
        repository = new FilebasedUserManagementRepository();
        config = new HashMap<>();
        config.put("users", "admin,user");
        config.put("admin.id", "#11d2741c");
        config.put("admin.roles", "admin,developer");
        config.put("admin.password", "adminpass");
        config.put("admin.email", "evandor@gmail.com");
        config.put("admin.name", "Carsten");
        config.put("admin.surname", "Gräf");

        config.put("user.roles", "developer,translator");
        config.put("user.password", "userpass");
        config.put("user.id", "#2");
    }

    @Test
    public void nothing_is_created_for_empty_configuration() {
        repository.activate(new HashMap<>());
        assertThat(repository.getUsers().size(), is(0));
        assertThat(repository.getRoles().size(), is(0));
        assertThat(repository.getUsersRoles().size(), is(0));
    }

    @Test
    public void creates_users_from_configuration() {
        repository.activate(config);
        assertThat(repository.getUsers().size(), is(2));
        assertThat(repository.getUsers().get("admin").getFirstName(),is("Carsten"));
        assertThat(repository.getUsers().get("admin").getLastName(),is("Gräf"));
        assertThat(repository.getUsers().get("admin").getEmail(),is("evandor@gmail.com"));
        assertThat(repository.getUsers().get("user").getSecret(),is("userpass".toCharArray()));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void creates_roles_from_configuration() {
        repository.activate(config);
        assertThat(repository.getRoles().size(), is(3));
        assertThat(repository.getRoles(), contains(new Role("admin"), new Role("developer"), new Role("translator")));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void creates_mapping_from_configuration() {
        repository.activate(config);
        assertThat(repository.getUsersRoles().size(), is(2));
        User admin = repository.getUser("admin").get();
        assertThat(repository.getUsersRoles().get(admin), containsInAnyOrder(new Role("admin"), new Role("developer")));
        User user = repository.getUser("user").get();
        assertThat(repository.getUsersRoles().get(user), containsInAnyOrder(new Role("developer"), new Role("translator")));
    }

    @Test
    public void more_users_defined_than_configured_ignores_additional_users() {
        config.put("users", "admin,user,tester");
        repository.activate(config);
        assertThat(repository.getUsers().size(), is(2));
    }

    @Test
    public void user_without_role_definition_yields_user_without_roles() {
        config.put("users", "admin,user,tester");
        config.put("tester.id","#3");
        config.put("tester.password","testerpassword");

        repository.activate(config);

        assertThat(repository.getUsers().size(), is(3));
        User tester = repository.getUser("tester").get();
        assertThat(repository.getUsersRoles().get(tester).size(), is(0));
    }

}
