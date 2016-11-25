package $basePackageName$;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;

import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.SkysailApplication;
import $basePackageName$.resources.BookmarkResource;
import $basePackageName$.resources.BookmarksResource;
import $basePackageName$.resources.PostBookmarkResource;
import $basePackageName$.resources.PutBookmarkResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class TemplateApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "templateApp";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public TemplateApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("a skysail application");
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    @Override
    public void setRepositories(Repositories repos) {
        super.setRepositories(repos);
    }

    public void unsetRepositories(Repositories repo) { // NOSONAR
        super.setRepositories(null);
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
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
        router.attach(new RouteBuilder("", BookmarksResource.class));
        
        router.attach(createStaticDirectory());
    }

}