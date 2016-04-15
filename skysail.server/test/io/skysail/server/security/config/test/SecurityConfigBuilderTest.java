package io.skysail.server.security.config.test;

import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import io.skysail.server.app.ApiVersion;
import io.skysail.server.security.config.AlwaysAuthenticatedAuthenticator;
import io.skysail.server.security.config.PathToAuthenticatorMatcherRegistry;
import io.skysail.server.security.config.SecurityConfig;
import io.skysail.server.security.config.SecurityConfigBuilder;

import static org.hamcrest.CoreMatchers.*;

public class SecurityConfigBuilderTest {


	private SecurityConfigBuilder configBuilder;

	@Before
	public void setUp() {
		configBuilder = new SecurityConfigBuilder(new ApiVersion(1));
	}
	
	@Test
	public void testName() throws Exception {
		assertThat(configBuilder.authorizeRequests(), instanceOf(PathToAuthenticatorMatcherRegistry.class));
	}
	
	@Test
	public void builds_simple_securityConfig_which_permits_access() {
		configBuilder
			.authorizeRequests()
				.startsWithMatcher("/abc").permitAll();
		SecurityConfig securityConfig = configBuilder.build();
		assertThat(securityConfig.authenticatorFor(null, "/v1/abcdef").getClass().getName(), is(AlwaysAuthenticatedAuthenticator.class.getName()));
	}
	
//	securityConfigBuilder
////	.exceptionHandling()
////    .authenticationEntryPoint(spnegoEntryPoint())
////    .and()
//		.authorizeRequests()
//			.antMatchers("7/").anonymous()
//			.anyRequest().authenticated()
//			.and()
//		.httpBasic();
}
