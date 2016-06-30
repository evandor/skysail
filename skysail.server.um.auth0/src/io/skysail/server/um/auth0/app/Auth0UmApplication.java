package io.skysail.server.um.auth0.app;

import org.osgi.service.component.annotations.Component;

import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(property = "name=Auth0UmApplication")
public class Auth0UmApplication extends SkysailApplication implements ApplicationProvider {

	@Override
	protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
		securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
	}

	@Override
	protected void attach() {	
      router.attach(new RouteBuilder(SkysailRootApplication.LOGIN_PATH, Auth0LoginPage.class));
      router.attach(new RouteBuilder(SkysailRootApplication.LOGOUT_PATH, Auth0LogoutPage.class));
      
      router.attach(createStaticDirectory());
	}

}
