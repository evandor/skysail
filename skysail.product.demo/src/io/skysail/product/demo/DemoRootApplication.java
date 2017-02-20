package io.skysail.product.demo;

import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

public class DemoRootApplication extends SkysailApplication implements ApplicationProvider {

    public DemoRootApplication() {
        super("demoproduct", null);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
    }

    @Override
    protected void attach() {
        router.attach(new RouteBuilder("", PublicResource.class));
    }
}

