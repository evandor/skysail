package io.skysail.server.app.fencerio;


import org.apache.camel.core.osgi.OsgiDefaultCamelContext;
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
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
@Slf4j
public class FencerIoApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "fencerio";

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;
    private OsgiDefaultCamelContext camelContext;

    public FencerIoApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("StarMoney Reporting");
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
//                .authorizeRequests().startsWithMatcher("/mailgun").permitAll().and()
//                .authorizeRequests().equalsMatcher("/Bookmarks/").permitAll().and()
//                .authorizeRequests().startsWithMatcher("/unprotected").permitAll().and()
                .authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();

        // router.attach(new RouteBuilder("/Bookmarks/{id}",
        // BookmarkResource.class));
        // router.attach(new RouteBuilder("/Bookmarks/",
        // PostBookmarkResource.class));
        // router.attach(new RouteBuilder("/Bookmarks/{id}/",
        // PutBookmarkResource.class));
//         router.attach(new io.skysail.server.restlet.RouteBuilder("", TransactionsResource.class));
         router.attach(new io.skysail.server.restlet.RouteBuilder("", GeofencesResource.class));
         router.attach(new io.skysail.server.restlet.RouteBuilder("/geofence", GeofencesResource.class));
        // router.attach(new RouteBuilder("", BookmarksResource.class));
    }

}