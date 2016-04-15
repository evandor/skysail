package io.skysail.server.security.config.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.skysail.api.um.AuthenticationService;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.security.config.AlwaysAuthenticatedAuthenticator;
import io.skysail.server.security.config.AuthenticatedAuthenticator;
import io.skysail.server.security.config.PathToAuthenticatorMatcherRegistry;
import io.skysail.server.security.config.NeverAuthenticatedAuthenticator;

public class PathToAuthenticatorMatcherRegistryTest {

	private PathToAuthenticatorMatcherRegistry registry;

	@Before
	public void setUp() {
		registry = new PathToAuthenticatorMatcherRegistry(new ApiVersion(1));
	}
	
	@Test
	public void start_with_matcher_references_authenticated_authenticator() {
		registry.startsWithMatcher("/abc").authenticated();
		assertThat(registry.getMatchers().size(), is(1));
		AuthenticationService authService = Mockito.mock(AuthenticationService.class);
		Mockito.when(authService.getAuthenticator(null)).thenReturn(new AuthenticatedAuthenticator(null));
		assertThat(registry.getMatchers().get(0).getAuthenticator(null,authService).getClass().getName(),is(AuthenticatedAuthenticator.class.getName()));
	}
	
	@Test
	public void start_with_matcher_references_permitAll_authenticator() {
		registry.startsWithMatcher("/abc").permitAll();
		assertThat(registry.getMatchers().size(), is(1));
		assertThat(registry.getMatchers().get(0).getAuthenticator(null,null).getClass().getName(),is(AlwaysAuthenticatedAuthenticator.class.getName()));
	}
	
	@Test
	public void start_with_matcher_references_denyAll_authenticator() {
		registry.startsWithMatcher("/abc").denyAll();
		assertThat(registry.getMatchers().size(), is(1));
		assertThat(registry.getMatchers().get(0).getAuthenticator(null,null).getClass().getName(),is(NeverAuthenticatedAuthenticator.class.getName()));
	}
}
