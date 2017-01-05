package io.skysail.server.app.ref.fields;

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
import io.skysail.server.app.ref.fields.repositories.EntityWithoutTabsRepo;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class FieldsDemoApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "fieldsdemo";

    @Reference
    private DbService dbService;

    @Getter
	private EntityWithoutTabsRepo repo;

    public FieldsDemoApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(EntityWithoutTabs.class));
        setDescription("a skysail application");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        //this.repo = new FieldsDemoRepository(dbService);
        this.repo = new EntityWithoutTabsRepo(dbService);
       // this.entitiesWithTabsRepo = new EntityWithTabsRepo(dbService);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();

/*
        router.attach(new RouteBuilder("/Bookmarks/{id}", BookmarkResource.class));
        router.attach(new RouteBuilder("/Bookmarks/", PostBookmarkResource.class));
        router.attach(new RouteBuilder("/Bookmarks/{id}/", PutBookmarkResource.class));
        router.attach(new RouteBuilder("/Bookmarks", BookmarksResource.class));

        router.attach(new RouteBuilder("/entityWoTabs/{id}", EntityWithoutTabResource.class));
        router.attach(new RouteBuilder("/entityWoTabs/", PostEntityWithoutTabsResource.class));
        router.attach(new RouteBuilder("/entityWoTabs/{id}/", PutEntityWithoutTabsResource.class));
        router.attach(new RouteBuilder("/entityWoTabs", EntitiesWithoutTabsResource.class));

        router.attach(new RouteBuilder("", BookmarksResource.class));
*/
    }

}