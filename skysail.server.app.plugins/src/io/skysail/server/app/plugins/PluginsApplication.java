package io.skysail.server.app.plugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.felix.bundlerepository.Repository;
import org.apache.felix.bundlerepository.RepositoryAdmin;
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
    private AtomicReference<RepositoryAdmin> repositoryAdmin = new AtomicReference<>();

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    private volatile EventAdmin eventAdmin;

    public PluginsApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("a skysail application");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        try {
			this.repositoryAdmin.get().addRepository("https://raw.githubusercontent.com/evandor/skysail/master/cnf/releaserepo/index.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public void setRepositoryAdmin(RepositoryAdmin repositoryAdmin) {
        this.repositoryAdmin.set(repositoryAdmin);
    }

    public void unsetRepositoryAdmin(RepositoryAdmin repositoryAdmin) {
        this.repositoryAdmin.compareAndSet(repositoryAdmin, null);
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
        if (repositoryAdmin.get() == null) {
            return Collections.emptyList();
        }
        Repository[] repos = repositoryAdmin.get().listRepositories();
        return Arrays.stream(repos)
                .map(ObrRepository::new)
                .collect(Collectors.toList());
    }

    public List<ObrResource> getResources(String id) {
        List<ObrResource> result = new ArrayList<>();
        getReposList().stream().filter(r -> r.getId().equals(id)).findFirst().ifPresent(repo -> {
            result.addAll(repo.getResources());
        });
        return result;
    }


}