package $basePackageName$;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import $basePackageName$.repositories.AggregateRootEntityRepo;

import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class TemplateApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "templateApp";

    @Reference
    private DbService dbService;

    @Getter
    private AggregateRootEntityRepo repo;
    
    public TemplateApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(AggregateRootEntity.class));
        setDescription("a skysail application");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        this.repo = new AggregateRootEntityRepo(dbService);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests().startsWithMatcher("").authenticated();
    }
//
//    @Override
//    protected void attach() {
//        super.attach();
//
//        router.attach(new RouteBuilder("/Bookmarks/{id}", BookmarkResource.class));
//        router.attach(new RouteBuilder("/Bookmarks/", PostBookmarkResource.class));
//        router.attach(new RouteBuilder("/Bookmarks/{id}/", PutBookmarkResource.class));
//        router.attach(new RouteBuilder("/Bookmarks", BookmarksResource.class));
//        router.attach(new RouteBuilder("", BookmarksResource.class));
//        
//    }

}