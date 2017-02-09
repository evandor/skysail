package io.skysail.server.security.config.test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.skysail.api.um.AuthenticationService;
import io.skysail.core.app.ApiVersion;
import io.skysail.server.security.config.AbstractPathToAuthenticatorMatcher;
import io.skysail.server.security.config.AlwaysAuthenticatedAuthenticator;
import io.skysail.server.security.config.AuthenticatedAuthenticator;
import io.skysail.server.security.config.NeverAuthenticatedAuthenticator;
import io.skysail.server.security.config.SecurityConfigBuilder;

public class AbstractPathToAuthenticatorMatcherTest {

	private AbstractPathToAuthenticatorMatcher matcher;

	@Before
	public void setUp() {
		SecurityConfigBuilder securityConfigBuilder = new SecurityConfigBuilder(new ApiVersion(1));
		matcher = new AbstractPathToAuthenticatorMatcher(securityConfigBuilder) {
			@Override
			public boolean match(String path) {
				return false;
			}
		};
	}

	@Test
	public void testPermitAll() {
		matcher.permitAll();
		assertThat(matcher.getAuthenticator(null, null), instanceOf(AlwaysAuthenticatedAuthenticator.class));
	}

	@Test
	public void testAuthenticated() throws Exception {
		matcher.authenticated();
		AuthenticationService authService = Mockito.mock(AuthenticationService.class);
		Mockito.when(authService.getResourceAuthenticator(null)).thenReturn(new AuthenticatedAuthenticator(null));
		assertThat(matcher.getAuthenticator(null, authService), instanceOf(AuthenticatedAuthenticator.class));
	}

	@Test
	public void testDenyAll() throws Exception {
		matcher.denyAll();
		assertThat(matcher.getAuthenticator(null, null), instanceOf(NeverAuthenticatedAuthenticator.class));
	}


}
