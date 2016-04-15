package io.skysail.server.security.config.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.security.Authenticator;

import io.skysail.server.security.config.AlwaysAuthenticatedAuthenticator;
import io.skysail.server.security.config.SecurityConfig;
import io.skysail.server.security.config.SecurityConfigMode;
import io.skysail.server.security.config.NeverAuthenticatedAuthenticator;

public class SecurityConfigTest {

	private SecurityConfig securityConfig;

	@Before
	public void setUp() {
		securityConfig = new SecurityConfig(null);
	}
	
	@Test
	public void always_matches_with_UnauthenticatedAuthenticator() {
		Authenticator authenticator = securityConfig.authenticatorFor(null, "somepath");
		assertThat(authenticator.getClass().getName(),is(NeverAuthenticatedAuthenticator.class.getName()));
	}

	@Test
	@Ignore
	public void matched_unprotected() throws Exception {
		securityConfig.match("/unprotected", SecurityConfigMode.PERMIT_ALL);
		Authenticator authenticator = securityConfig.authenticatorFor(null, "/unprotected");
		assertThat(authenticator.getClass().getName(),is(AlwaysAuthenticatedAuthenticator.class.getName()));
		
	}
	
	@Test
	@Ignore
	public void matched_protected() throws Exception {
		securityConfig.match("/protected", SecurityConfigMode.DENY_ALL);
		securityConfig.match("/unprotected", SecurityConfigMode.PERMIT_ALL);
		
		assertThat(authenticatorClass("/protected").getName(),is(NeverAuthenticatedAuthenticator.class.getName()));
		assertThat(authenticatorClass("/unprotected").getName(),is(AlwaysAuthenticatedAuthenticator.class.getName()));
	}

	private Class<? extends Authenticator> authenticatorClass(String path) {
		return securityConfig.authenticatorFor(null, path).getClass();
	}

}
