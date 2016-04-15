package io.skysail.server.security.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.skysail.server.security.SecurityContext;
import io.skysail.server.security.token.AnonymousAuthenticationToken;

public class SecurityContextTest {

	@Test
	public void defaults_to_anonymous_authentication() {
		SecurityContext securityContext = new SecurityContext();
		assertThat(securityContext.getAuthentication(),instanceOf(AnonymousAuthenticationToken.class));
	}
	
	@Test
	public void testName() {
		SecurityContext securityContext = new SecurityContext();
		assertThat(securityContext.toString(),containsString("SecurityContext"));
		assertThat(securityContext.toString(),containsString(AnonymousAuthenticationToken.class.getName()));
	}
}
