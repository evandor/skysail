package io.skysail.server.security.config.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.ChallengeScheme;
import org.restlet.security.Authenticator;

import io.skysail.server.security.config.SecurityConfig;
import io.skysail.server.security.config.UnauthenticatedAuthenticator;

public class SecurityConfigTest {

	private SecurityConfig securityConfig;

	@Before
	public void setUp() {
		securityConfig = new SecurityConfig();
	}
	
	@Test
	public void always_matches_with_UnauthenticatedAuthenticator() {
		Authenticator authenticator = securityConfig.authenticatorFor(null, "somepath");
		assertThat(authenticator.getClass().getName(),is(UnauthenticatedAuthenticator.class.getName()));
	}

	@Test
	public void matched_unprotected() throws Exception {
		securityConfig.match("/unprotected",SecurityConfig.PERMIT_ALL);
		Authenticator authenticator = securityConfig.authenticatorFor(null, "/unprotected");
		assertThat(authenticator.getClass().getName(),is(AlwaysAuthenticatedAuthenticator.class.getName()));
		
	}
}
