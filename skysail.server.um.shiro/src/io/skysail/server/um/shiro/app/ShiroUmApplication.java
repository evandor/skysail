package io.skysail.server.um.shiro.app;

import org.osgi.service.component.annotations.Component;

import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(property = "name=ShiroUmApplication")
public class ShiroUmApplication extends SkysailApplication implements ApplicationProvider {

    public ShiroUmApplication() {
		super(ShiroUmApplication.class.getName());
	}

	@Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests().startsWithMatcher(SkysailRootApplication.LOGIN_PATH).permitAll();
    }

    @Override
    protected void attach() {
        router.attach(new RouteBuilder(SkysailRootApplication.LOGIN_PATH, LoginResource.class), true);
        router.attach(new RouteBuilder("/currentUser", CurrentUserResource.class), true);
    }

}