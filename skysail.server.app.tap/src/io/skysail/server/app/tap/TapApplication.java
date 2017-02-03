package io.skysail.server.app.tap;

import java.util.Arrays;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.app.tap.repositories.DocumentRepository;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class TapApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "TAP";

    @Reference
    private DbService dbService;

    @Getter
    private DocumentRepository repository;

    public TapApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(Document.class));
        setDescription("text analyser platform");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        this.repository = new DocumentRepository(dbService);
    }

}