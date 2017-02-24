package io.skysail.server.um.shiro.authentication;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Context;
import org.restlet.security.Authenticator;

import io.skysail.api.um.AuthenticationMode;
import io.skysail.server.um.shiro.authentication.ShiroAuthenticationService;

public class ShiroAuthenticationServiceTest {

    @Test
    public void testName() {
        ShiroAuthenticationService service = new ShiroAuthenticationService(null);
        Context context = Mockito.mock(Context.class);
        Authenticator authenticator = service.getResourceAuthenticator(context, AuthenticationMode.AUTHENTICATED);
        assertThat(authenticator, is(notNullValue()));
    }
}
