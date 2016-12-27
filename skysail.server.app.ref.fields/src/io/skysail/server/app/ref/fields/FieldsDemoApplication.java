package io.skysail.server.app.ref.fields;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;


import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.db.DbService;
import io.skysail.server.app.ref.fields.resources.BookmarkResource;
import io.skysail.server.app.ref.fields.resources.BookmarksResource;
import io.skysail.server.app.ref.fields.resources.EntitiesWithoutTabsResource;
import io.skysail.server.app.ref.fields.resources.EntityWithoutTabResource;
import io.skysail.server.app.ref.fields.resources.PostBookmarkResource;
import io.skysail.server.app.ref.fields.resources.PostEntityWithoutTabResource;
import io.skysail.server.app.ref.fields.resources.PutBookmarkResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class FieldsDemoApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "fieldsdemo";

    @Reference
    private DbService dbService;

    @Getter
    private FieldsDemoRepository repo;

    @Getter
	private EntitiesWoTabsRepository entitiesWoTabsRepo;

    public FieldsDemoApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("a skysail application");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        this.repo = new FieldsDemoRepository(dbService);
        this.entitiesWoTabsRepo = new EntitiesWoTabsRepository(dbService);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("/Bookmarks/{id}", BookmarkResource.class));
        router.attach(new RouteBuilder("/Bookmarks/", PostBookmarkResource.class));
        router.attach(new RouteBuilder("/Bookmarks/{id}/", PutBookmarkResource.class));
        router.attach(new RouteBuilder("/Bookmarks", BookmarksResource.class));

        router.attach(new RouteBuilder("/entityWoTabs/{id}", EntityWithoutTabResource.class));
        router.attach(new RouteBuilder("/entityWoTabs/", PostEntityWithoutTabResource.class));
        //router.attach(new RouteBuilder("/entityWoTabs/{id}/", PutBookmarkResource.class));
        router.attach(new RouteBuilder("/entityWoTabs", EntitiesWithoutTabsResource.class));

        router.attach(new RouteBuilder("", BookmarksResource.class));

        router.attach(createStaticDirectory());
    }

}