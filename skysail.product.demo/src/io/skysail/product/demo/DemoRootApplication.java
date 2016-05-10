package io.skysail.product.demo;

import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.RouteBuilder;

public class DemoRootApplication extends SkysailApplication implements ApplicationProvider {

    public DemoRootApplication() {
        super("demoproduct", null);
    }
    
    @Override
    protected void attach() {
        router.attach(new RouteBuilder("", PublicResource.class).noAuthenticationNeeded());
    }
}

