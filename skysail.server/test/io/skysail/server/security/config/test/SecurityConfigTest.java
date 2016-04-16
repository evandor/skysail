package io.skysail.server.security.config.test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.restlet.security.Authenticator;

import io.skysail.server.app.ApiVersion;
import io.skysail.server.security.config.AlwaysAuthenticatedAuthenticator;
import io.skysail.server.security.config.NeverAuthenticatedAuthenticator;
import io.skysail.server.security.config.PathToAuthenticatorMatcher;
import io.skysail.server.security.config.SecurityConfig;
import io.skysail.server.security.config.SecurityConfigBuilder;
import io.skysail.server.security.config.StartsWithExpressionPathToAuthenticatorMatcher;

public class SecurityConfigTest {

	private SecurityConfig securityConfig;
	private SecurityConfigBuilder securityConfigBuilder;

	@Before
	public void setUp() {
		securityConfig = new SecurityConfig(null);
		securityConfigBuilder = new SecurityConfigBuilder(new ApiVersion(1));
	}
	
	@Test
	public void always_matches_with_UnauthenticatedAuthenticator() {
		Authenticator authenticator = securityConfig.authenticatorFor(null, "somepath");
		assertThat(authenticator.getClass().getName(),is(NeverAuthenticatedAuthenticator.class.getName()));
	}

	@Test
	public void matches_unprotected() {
		PathToAuthenticatorMatcher matcher = new StartsWithExpressionPathToAuthenticatorMatcher(securityConfigBuilder, "/unprotected");
		matcher.permitAll();
		securityConfig.match(matcher);

		Authenticator authenticator = securityConfig.authenticatorFor(null, "/v1/unprotected");
		
		assertThat(authenticator,instanceOf(AlwaysAuthenticatedAuthenticator.class));
	}
	
	@Test
	public void matches_protected() throws Exception {
		PathToAuthenticatorMatcher matcher = new StartsWithExpressionPathToAuthenticatorMatcher(securityConfigBuilder, "/unprotected");
		matcher.permitAll();
		securityConfig.match(matcher);

		PathToAuthenticatorMatcher matcher2 = new StartsWithExpressionPathToAuthenticatorMatcher(securityConfigBuilder, "/protected");
		matcher2.denyAll();
		securityConfig.match(matcher2);

		Authenticator authenticator = securityConfig.authenticatorFor(null, "/v1/unprotected");
		assertThat(authenticator,instanceOf(AlwaysAuthenticatedAuthenticator.class));

		Authenticator authenticator2 = securityConfig.authenticatorFor(null, "/v1/protected");
		assertThat(authenticator2,instanceOf(NeverAuthenticatedAuthenticator.class));
	}

	private Class<? extends Authenticator> authenticatorClass(String path) {
		return securityConfig.authenticatorFor(null, path).getClass();
	}

}
