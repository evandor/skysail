package io.skysail.server.app.routes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import io.skysail.core.app.SkysailApplication;
import io.skysail.server.app.ApiVersion;
import io.skysail.server.app.ApplicationConfiguration;
import io.skysail.server.app.ApplicationProvider;
import io.skysail.server.app.routes.resources.RoutesResource;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class RoutesApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "routes";

    @Reference
    private DbService dbService;
    
    private Set<ApplicationProvider> applicationProvider = new HashSet<>();
    
    public RoutesApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("a skysail application");
    }
    
    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    void addApplicationProvider(ApplicationProvider provider) {
        applicationProvider.add(provider);
    }

    void removeApplicationProvider(ApplicationProvider provider) {
        applicationProvider.remove(provider);
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
        router.attach(new RouteBuilder("/routes", RoutesResource.class));
        router.attach(new RouteBuilder("", RoutesResource.class));
    }

    public List<RouteDescription> getRoutesList() {
        List<RouteDescription> routes = new ArrayList<>();
        List<SkysailApplication> apps = applicationProvider.stream()
                .map(ApplicationProvider::getApplication)
                .sorted((a1,a2) -> a1.getName().compareTo(a2.getName()))
                .collect(Collectors.toList());
        apps.forEach(app -> {
            List<RouteDescription> appRoutes = app.getRoutesMap().keySet().stream()
                    .map(key -> new RouteDescription(app.getName(), key, app.getRoutesMap().get(key)))
                    .collect(Collectors.toList());
            routes.addAll(appRoutes);
        });
        return routes;
    }
    
    @Override
	public List<MenuItem> getMenuEntries() {
		MenuItem appMenu = new MenuItem(getName(), "/" + getName() + getApiVersion().getVersionPath());
		appMenu.setCategory(MenuItem.Category.ADMIN_MENU);
		return Arrays.asList(appMenu);
	}


}