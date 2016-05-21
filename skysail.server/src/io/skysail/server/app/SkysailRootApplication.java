package io.skysail.server.app;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.event.EventAdmin;
import org.restlet.Request;

import io.skysail.server.app.resources.DefaultResource;
import io.skysail.server.app.resources.LoginResource;
import io.skysail.server.app.resources.LogoutResource;
import io.skysail.server.app.resources.ProfileResource;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.menus.MenuItem.Category;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;
import io.skysail.server.services.ResourceBundleProvider;
import io.skysail.server.utils.MenuItemUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = { "service.pid=landingpages" })
@Slf4j
public class SkysailRootApplication extends SkysailApplication
        implements ApplicationProvider, ResourceBundleProvider, ManagedService {

    private static final String CONFIG_IDENTIFIER_LANDINGPAGE_NOT_AUTHENTICATED = "landingPage.notAuthenticated";
    private static final String CONFIG_IDENTIFIER_LANDINGPAGE_AUTHENTICATED = "landingPage.authenticated";

    private static final String ROOT_APPLICATION_NAME = "root";

    public static final String LOGIN_PATH = "/_login";
    public static final String PROFILE_PATH = "/_profile";
    public static final String PUPLIC_PATH = "/_public";
    public static final String LOGOUT_PATH = "/_logout";

    private volatile Set<SkysailApplication> applications = new TreeSet<>();

    private Set<MenuItemProvider> menuProviders = new HashSet<>();

    private Dictionary<String, ?> properties;

    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    public SkysailRootApplication() {
        super(ROOT_APPLICATION_NAME, null);
    }

    @Override
    @Activate
    protected synchronized void activate(ComponentContext componentContext) {
        if (getContext() != null) {
            setContext(getContext().createChildContext());
        }
        setComponentContext(componentContext);
        dumpBundlesInformationToLog(componentContext.getBundleContext().getBundles());
    }

    @Override
    @Deactivate
    protected synchronized void deactivate(ComponentContext componentContext) {
        setComponentContext(null);
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.OPTIONAL)
    public void setApplicationListProvider(ServiceListProvider service) {
        SkysailRootApplication.setServiceListProvider(service);
    }

    public void unsetApplicationListProvider(ServiceListProvider service) {
        SkysailRootApplication.unsetServiceListProvider(service);
    }

    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder
            .authorizeRequests()
                .startsWithMatcher("").permitAll();
    }

    @Override
    protected void attach() {
        router.attach(new RouteBuilder("/", DefaultResource.class));
        router.attach(new RouteBuilder(LOGIN_PATH, LoginResource.class));
        router.attach(new RouteBuilder(LOGOUT_PATH, LogoutResource.class));
        router.attach(new RouteBuilder(PROFILE_PATH, ProfileResource.class));
    }

    public Set<SkysailApplication> getApplications() {
        return this.applications;
    }

    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
    public void addMenuProvider(MenuItemProvider provider) {
        menuProviders.add(provider);
    }

    public void removeMenuProvider(MenuItemProvider provider) {
        menuProviders.remove(provider);
    }

    public Set<MenuItem> getMenuItems() {
        return menuProviders.stream()//
                .map(mp -> mp.getMenuEntries())//
                .filter(l -> l != null)//
                .flatMap(mil -> mil.stream())//
                .collect(Collectors.toSet());
    }

    public String getRedirectTo(DefaultResource defaultResource) {
        if (properties == null) {
            return null;
        }
        if (!isAuthenticated(defaultResource.getRequest())) {
            return (String) properties.get(CONFIG_IDENTIFIER_LANDINGPAGE_NOT_AUTHENTICATED);
        }
        String landingPage = (String) properties.get(CONFIG_IDENTIFIER_LANDINGPAGE_AUTHENTICATED);
        if (landingPage == null || "".equals(landingPage) || "/".equals(landingPage)) {
            return null;
        }
        return landingPage;
    }

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        this.properties = properties;
    }

    public void clearCache(String username) {
    }

    public Set<MenuItem> getMainMenuItems(DefaultResource resource, Request request) {
        return MenuItemUtils.getMenuItems(menuProviders, resource, Category.APPLICATION_MAIN_MENU);
    }

    private void dumpBundlesInformationToLog(Bundle[] bundles) {
        log.debug("Currently installed bundles:");
        log.debug("----------------------------");
        log.debug("");
        Arrays.stream(bundles) // NOSONAR
                .forEach(b -> log.debug("{} [{}] state {}", b.getSymbolicName(), b.getVersion(), b.getState()));
    }
}
