package io.skysail.server.um.httpbasic.app;

import org.osgi.service.component.annotations.Component;

import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(property = "name=HttpBasicUmApplication")
public class HttpBasicUmApplication extends SkysailApplication implements ApplicationProvider {

	public HttpBasicUmApplication() {
		super(HttpBasicUmApplication.class.getName());
	}

	@Override
	protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
		securityConfigBuilder.authorizeRequests().startsWithMatcher("").authenticated();
	}

	@Override
	protected void attach() {
        router.attach(new RouteBuilder(SkysailRootApplication.LOGIN_PATH, HttpBasicLoginPage.class), true);
        router.attach(new RouteBuilder(SkysailRootApplication.LOGOUT_PATH, HttpBasicLogoutPage.class), true);
	}

}
