package io.skysail.client.ng2;

import org.osgi.service.component.annotations.Component;

import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;

@Component(immediate = true)
public class ClientRootApplication extends SkysailApplication implements ApplicationProvider {

    public ClientRootApplication() {
        super("client");
    }

    @Override
    protected void attach() {
        router.attach(createStaticDirectory());
    }
}
