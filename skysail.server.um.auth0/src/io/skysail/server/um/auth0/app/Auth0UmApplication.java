package io.skysail.server.um.auth0.app;

import org.osgi.service.component.annotations.Component;

import io.skysail.core.app.SkysailApplication;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;
import io.skysail.server.um.auth0.Auth0Client;
import io.skysail.server.um.auth0.Tokens;
import lombok.Setter;

@Component(property = "name=Auth0UmApplication")
public class Auth0UmApplication extends SkysailApplication implements ApplicationProvider {

    public Auth0UmApplication(String appName) {
		super(Auth0UmApplication.class.getName());
	}

	@Setter
    private Auth0Client auth0Client;

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
    }

    @Override
    protected void attach() {
        router.attach(new RouteBuilder(SkysailRootApplication.LOGIN_PATH, Auth0LoginPage.class));
        router.attach(new RouteBuilder(SkysailRootApplication.LOGOUT_PATH, Auth0LogoutPage.class));
        router.attach(new RouteBuilder(SkysailRootApplication.LOGIN_CALLBACK, Auth0LoginCallbackPage.class));

    }

    public Tokens getTokens(String authorizationCode, String redirectUri) {
        return auth0Client.getTokens(authorizationCode, redirectUri);
    }

}
