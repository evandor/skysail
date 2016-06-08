package io.skysail.server.um.shiro;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import io.skysail.api.um.AuthenticationService;
import io.skysail.server.um.shiro.ShiroBasedUserManagementProvider;

@Ignore
public class FileBasedUserManagementProviderTest {

    private ShiroBasedUserManagementProvider provider;

    @Before
    public void setUp() throws Exception {
        provider = new ShiroBasedUserManagementProvider();
    }

    @Test
    public void authenticationService_is_available_for_activated_UserManagmentProvider() {
        AuthenticationService service = provider.getAuthenticationService();
        assertThat(service, is(notNullValue()));
    }

    @Test
    public void authorizationService_is_available_for_activated_UserManagmentProvider() {
        //AuthorizationService service = provider.getAuthorizationService();
        //assertThat(service, is(notNullValue()));
    }

    @Test
    public void deactivation_nulls_services() throws Exception {
        provider.deactivate();
        assertThat(provider.getAuthenticationService(), is(nullValue()));
        assertThat(provider.getAuthorizationService(), is(nullValue()));
    }
}
