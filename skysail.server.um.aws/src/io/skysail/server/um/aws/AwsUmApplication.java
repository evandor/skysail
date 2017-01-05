package io.skysail.server.um.aws;

import org.osgi.service.component.annotations.Component;

import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(property = "name=AwsUmApplication")
public class AwsUmApplication extends SkysailApplication implements ApplicationProvider {

    public AwsUmApplication() {
        super(AwsUmApplication.class.getName());
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests()
            .startsWithMatcher(SkysailRootApplication.LOGIN_PATH).permitAll()
            .startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        router.attach(new RouteBuilder(SkysailRootApplication.LOGIN_PATH, AwsLoginPage.class), true);
        router.attach(new RouteBuilder(SkysailRootApplication.LOGOUT_PATH, AwsLogoutPage.class), true);
    }

}
