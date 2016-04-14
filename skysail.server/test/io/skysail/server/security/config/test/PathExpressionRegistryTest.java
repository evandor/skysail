package io.skysail.server.security.config.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import io.skysail.server.security.config.AlwaysAuthenticatedAuthenticator;
import io.skysail.server.security.config.AuthenticatedAuthenticator;
import io.skysail.server.security.config.PathExpressionRegistry;
import io.skysail.server.security.config.UnauthenticatedAuthenticator;

public class PathExpressionRegistryTest {

	private PathExpressionRegistry registry;

	@Before
	public void setUp() {
		registry = new PathExpressionRegistry();
	}
	
	@Test
	public void start_with_matcher_references_authenticated_authenticator() {
		registry.startsWithMatcher("/abc").authenticated();
		assertThat(registry.getEntries().size(), is(1));
		assertThat(registry.getEntries().get(0).getAuthenticator(null).getClass().getName(),is(AuthenticatedAuthenticator.class.getName()));
	}
	
	@Test
	public void start_with_matcher_references_permitAll_authenticator() {
		registry.startsWithMatcher("/abc").permitAll();
		assertThat(registry.getEntries().size(), is(1));
		assertThat(registry.getEntries().get(0).getAuthenticator(null).getClass().getName(),is(AlwaysAuthenticatedAuthenticator.class.getName()));
	}
	
	@Test
	public void start_with_matcher_references_denyAll_authenticator() {
		registry.startsWithMatcher("/abc").denyAll();
		assertThat(registry.getEntries().size(), is(1));
		assertThat(registry.getEntries().get(0).getAuthenticator(null).getClass().getName(),is(UnauthenticatedAuthenticator.class.getName()));
	}
}
