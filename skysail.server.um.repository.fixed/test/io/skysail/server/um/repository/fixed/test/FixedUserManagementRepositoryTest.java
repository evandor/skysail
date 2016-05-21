package io.skysail.server.um.repository.fixed.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.restlet.security.Role;
import org.restlet.security.User;

import io.skysail.server.um.repository.fixed.FixedUserManagementRepository;

public class FixedUserManagementRepositoryTest {

    private FixedUserManagementRepository repository;

    @Before
    public void setUp() {
        repository = new FixedUserManagementRepository();
    }

    @Test
    public void getting_user_echos_username() {
        assertThat(repository.getUser("username").get().getName(),is("username"));
        assertThat(repository.getUser("username").get().getLastName(),is("lastname"));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void getting_roles() {
        repository.getUser("username");
        assertThat(repository.getRoles(),containsInAnyOrder(new Role("user"), new Role("username")));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void getting_userRoles() {
        User user = repository.getUser("username").get();
        assertThat(repository.getUsersRoles().get(user),containsInAnyOrder(new Role("user"), new Role("username")));
    }
}
