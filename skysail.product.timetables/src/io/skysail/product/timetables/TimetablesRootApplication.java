package io.skysail.product.timetables;

import org.osgi.service.component.annotations.Component;

import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.RouteBuilder;

@Component(immediate = true)
public class TimetablesRootApplication extends SkysailApplication implements ApplicationProvider {

    public TimetablesRootApplication() {
        super("timetables", new ApiVersion(1));
    }

    @Override
    protected void attach() {
        router.attach(new RouteBuilder("", PublicResource.class));
    }
}

