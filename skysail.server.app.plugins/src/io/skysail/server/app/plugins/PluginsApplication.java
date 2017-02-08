package io.skysail.server.app.plugins;

import java.util.List;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.event.EventAdmin;

import io.skysail.core.app.SkysailApplication;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.plugins.features.FeaturesRepository;
import io.skysail.server.app.plugins.obr.ObrRepository;
import io.skysail.server.app.plugins.obr.ObrResource;
import io.skysail.server.app.plugins.obr.RepositoriesResource;
import io.skysail.server.app.plugins.obr.RepositoryResource;
import io.skysail.server.app.plugins.resources.ResourceResource;
import io.skysail.server.app.plugins.resources.ResourcesResource;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class PluginsApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String LIST_ID = "lid";
    public static final String TODO_ID = "id";
    public static final String APP_NAME = "plugins";

    private FeaturesRepository featuresRepository;
    
    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;
    
    @Reference
    private ObrService obrService;

    public PluginsApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("a skysail application");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
    }

    public synchronized FeaturesRepository getFeaturesRepository() {
        if (featuresRepository == null) {
            featuresRepository = new FeaturesRepository();
        }
        return featuresRepository;
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests().startsWithMatcher("").authenticated();
    }

    @Override
    protected void attach() {
        super.attach();

       // router.setAuthorizationDefaults(anyOf("admin"));

//        router.attach(new RouteBuilder("/features/", FeaturesResource.class));
//        router.attach(new RouteBuilder("/features/{id}/installations/", PostInstallationResource.class));
//
        router.attach(new RouteBuilder("", RepositoriesResource.class));

        router.attach(new RouteBuilder("/repos/", RepositoriesResource.class));
        router.attach(new RouteBuilder("/repos/{id}", RepositoryResource.class));
        router.attach(new RouteBuilder("/repos/{id}/resources", ResourcesResource.class));
        router.attach(new RouteBuilder("/repos/{id}/resources/{resourceId}", ResourceResource.class));
//
//        router.attach(new RouteBuilder("/resolver/", PostResolverResource.class));
//
//        router.attach(new RouteBuilder("/query/", PostQueryResource.class));
//
//        router.attach(new RouteBuilder("/resources", OldResourcesResource.class));
//        router.attach(new RouteBuilder("/resources/{id}", OldResourceResource.class));

    }

    public List<ObrRepository> getReposList() {
        return obrService.getReposList();
    }

    public List<ObrResource> getResources(String id) {
        return obrService.getResources(id);
    }

   


}