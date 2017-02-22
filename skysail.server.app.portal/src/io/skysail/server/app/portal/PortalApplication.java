package io.skysail.server.app.portal;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.ApplicationConfiguration;
import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.server.app.portal.resources.BookmarkResource;
import io.skysail.server.app.portal.resources.PagesResource;
import io.skysail.server.app.portal.resources.PostBookmarkResource;
import io.skysail.server.app.portal.resources.PutBookmarkResource;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class PortalApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "portal";

    @Reference
    private DbService dbService;

    @Getter
    private PortalRepository repo;

    public PortalApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("a skysail application");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        this.repo = new PortalRepository(dbService);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("/pages/{id}", BookmarkResource.class));
        router.attach(new RouteBuilder("/pages/", PostBookmarkResource.class));
        router.attach(new RouteBuilder("/pages/{id}/", PutBookmarkResource.class));
        router.attach(new RouteBuilder("/pages", PagesResource.class));
        router.attach(new RouteBuilder("", PagesResource.class));

        router.attach(createStaticDirectory());
    }

}