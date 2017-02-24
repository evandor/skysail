package io.skysail.server.security.config.test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.restlet.security.Authenticator;

import io.skysail.api.um.AlwaysAuthenticatedAuthenticator;
import io.skysail.api.um.NeverAuthenticatedAuthenticator;
import io.skysail.core.app.ApiVersion;
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
        assertThat(authenticator.getClass().getName(), is(NeverAuthenticatedAuthenticator.class.getName()));
    }

//    @Test
//    public void matches_unprotected() {
//        PathToAuthenticatorMatcher matcher = new StartsWithExpressionPathToAuthenticatorMatcher(securityConfigBuilder,
//                "/unprotected");
//        matcher.permitAll();
//        securityConfig.addPathToAuthenticatorMatcher(matcher);
//
//        Authenticator authenticator = securityConfig.authenticatorFor(null, "/v1/unprotected");
//
//        assertThat(authenticator, instanceOf(AlwaysAuthenticatedAuthenticator.class));
//    }
//
//    @Test
//    public void matches_protected() throws Exception {
//        PathToAuthenticatorMatcher matcher = new StartsWithExpressionPathToAuthenticatorMatcher(securityConfigBuilder,
//                "/unprotected");
//        matcher.permitAll();
//        securityConfig.addPathToAuthenticatorMatcher(matcher);
//
//        PathToAuthenticatorMatcher matcher2 = new StartsWithExpressionPathToAuthenticatorMatcher(securityConfigBuilder,
//                "/protected");
//        matcher2.denyAll();
//        securityConfig.addPathToAuthenticatorMatcher(matcher2);
//
//        Authenticator authenticator = securityConfig.authenticatorFor(null, "/v1/unprotected");
//        assertThat(authenticator, instanceOf(AlwaysAuthenticatedAuthenticator.class));
//
//        Authenticator authenticator2 = securityConfig.authenticatorFor(null, "/v1/protected");
//        assertThat(authenticator2, instanceOf(NeverAuthenticatedAuthenticator.class));
//    }

//    @Test
//    public void toString_format() {
//        PathToAuthenticatorMatcher matcher = new StartsWithExpressionPathToAuthenticatorMatcher(securityConfigBuilder,
//                "/unprotected");
//        matcher.permitAll();
//        securityConfig.addPathToAuthenticatorMatcher(matcher);
//
//        assertThat(securityConfig.toString(), is("SecurityConfig:\n  StartsWithExpressionPathToAuthenticatorMatcher: /v1/unprotected -> class io.skysail.server.security.config.AlwaysAuthenticatedAuthenticator\n"));
//    }

}
