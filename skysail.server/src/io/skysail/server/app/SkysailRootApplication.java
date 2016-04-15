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

import de.twenty11.skysail.server.core.restlet.RouteBuilder;
import de.twenty11.skysail.server.resources.DefaultResource;
import de.twenty11.skysail.server.resources.HttpBasicLoginPage;
import de.twenty11.skysail.server.resources.HttpDigestLoginPage;
import de.twenty11.skysail.server.resources.LoginResource;
import io.skysail.server.menus.MenuItem;
import io.skysail.server.menus.MenuItem.Category;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.security.config.SecurityConfigBuilder;
import io.skysail.server.services.ResourceBundleProvider;
import io.skysail.server.utils.MenuItemUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = { "service.pid=landingpages" })
@Slf4j
public class SkysailRootApplication extends SkysailApplication implements ApplicationProvider, ResourceBundleProvider,
        ManagedService {

    private static final String CONFIG_IDENTIFIER_LANDINGPAGE_NOT_AUTHENTICATED = "landingPage.notAuthenticated";
    private static final String CONFIG_IDENTIFIER_LANDINGPAGE_AUTHENTICATED = "landingPage.authenticated";

    private static final String ROOT_APPLICATION_NAME = "root";

    public static final String LOGIN_PATH = "/_login";
    public static final String HTTP_BASIC_LOGIN_PATH = "/_httpbasic";
    public static final String HTTP_DIGEST_LOGIN_PATH = "/_httpdigest";
    public static final String DEMO_LOGIN_PATH = "/_demologin";
    public static final String PEERS_LOGIN_PATH = "/_remotelogin";
    public static final String PUPLIC_PATH = "/_public";
    public static final String LOGOUT_PATH = "/_logout";

    private volatile Set<SkysailApplication> applications = new TreeSet<>();

    private Set<MenuItemProvider> menuProviders = new HashSet<>();

    private Dictionary<String, ?> properties;
    
    @Reference(cardinality = ReferenceCardinality.OPTIONAL)
    @Getter
    private volatile EventAdmin eventAdmin;

    public SkysailRootApplication() {
        super(ROOT_APPLICATION_NAME,null);
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
        super.setServiceListProvider(service);
    }

    public void unsetApplicationListProvider(ServiceListProvider service) {
        super.unsetServiceListProvider(service);
    }
    
    @Override
    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
    	securityConfigBuilder
    		.authorizeRequests()
    			.startsWithMatcher(HTTP_BASIC_LOGIN_PATH).authenticated();
    		;
    }

    @Override
    protected void attach() {
        router.attach(new RouteBuilder("/", DefaultResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder(LOGIN_PATH, LoginResource.class).noAuthenticationNeeded());
        router.attach(new RouteBuilder(HTTP_BASIC_LOGIN_PATH, HttpBasicLoginPage.class));
        router.attach(new RouteBuilder(HTTP_DIGEST_LOGIN_PATH, HttpDigestLoginPage.class));
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
        //getAuthenticationService().clearCache(username);
    }

    public Set<MenuItem> getMainMenuItems(DefaultResource resource, Request request) {
//        Set<MenuItemProvider> providers = menuProviders.stream().map(m -> m.get()).collect(Collectors.toSet());
        return MenuItemUtils.getMenuItems(menuProviders, resource, Category.APPLICATION_MAIN_MENU);
    }

    private void dumpBundlesInformationToLog(Bundle[] bundles) {
        log.debug("Currently installed bundles:");
        log.debug("----------------------------");
        log.debug("");
        Arrays.stream(bundles).forEach(b -> log.debug("{} [{}] state {}", b.getSymbolicName(), b.getVersion(), b.getState()));
    }
}
