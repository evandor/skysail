package io.skysail.server.security.config.test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import io.skysail.core.app.ApiVersion;
import io.skysail.server.security.config.AlwaysAuthenticatedAuthenticator;
import io.skysail.server.security.config.NeverAuthenticatedAuthenticator;
import io.skysail.server.security.config.PathToAuthenticatorMatcherRegistry;
import io.skysail.server.security.config.SecurityConfig;
import io.skysail.server.security.config.SecurityConfigBuilder;

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
        configBuilder.authorizeRequests().startsWithMatcher("/abc").permitAll();
        SecurityConfig securityConfig = configBuilder.build();
        assertThat(securityConfig.authenticatorFor(null, "/v1/abcdef").getClass().getName(),
                is(AlwaysAuthenticatedAuthenticator.class.getName()));
    }

    @Test
    public void securityConfig_with_two_direct_following_matchers() {
        configBuilder.authorizeRequests().startsWithMatcher("/abc").permitAll().startsWithMatcher("/xxx").denyAll();
        SecurityConfig securityConfig = configBuilder.build();
        assertThat(securityConfig.authenticatorFor(null, "/v1/abcdef"),
                instanceOf(AlwaysAuthenticatedAuthenticator.class));
        assertThat(securityConfig.authenticatorFor(null, "/v1/xxx"), instanceOf(NeverAuthenticatedAuthenticator.class));
    }

    @Test
    public void securityConfig_with_two_matchers_defined_after_each_other() {
        configBuilder.authorizeRequests().startsWithMatcher("/abc").permitAll();
        configBuilder.authorizeRequests().startsWithMatcher("/xxx").denyAll();

        SecurityConfig securityConfig = configBuilder.build();

        assertThat(securityConfig.authenticatorFor(null, "/v1/abcdef"),
                instanceOf(AlwaysAuthenticatedAuthenticator.class));
        assertThat(securityConfig.authenticatorFor(null, "/v1/xxx"), instanceOf(NeverAuthenticatedAuthenticator.class));
    }

    @Test
    public void first_match_determines_authentication_scheme_part1() {
        configBuilder.authorizeRequests().startsWithMatcher("").denyAll();
        configBuilder.authorizeRequests().startsWithMatcher("/unprotected").permitAll();

        SecurityConfig securityConfig = configBuilder.build();

        assertThat(securityConfig.authenticatorFor(null, "/v1/protected"), instanceOf(NeverAuthenticatedAuthenticator.class));
        assertThat(securityConfig.authenticatorFor(null, "/v1/unprotected/xxx"), instanceOf(NeverAuthenticatedAuthenticator.class));
    }

    @Test
    public void first_match_determines_authentication_scheme_part2() {
        configBuilder.authorizeRequests().startsWithMatcher("/unprotected").permitAll();
        configBuilder.authorizeRequests().startsWithMatcher("").denyAll();

        SecurityConfig securityConfig = configBuilder.build();

        assertThat(securityConfig.authenticatorFor(null, "/v1/protected"), instanceOf(NeverAuthenticatedAuthenticator.class));
        assertThat(securityConfig.authenticatorFor(null, "/v1/unprotected"), instanceOf(AlwaysAuthenticatedAuthenticator.class));
    }

}
