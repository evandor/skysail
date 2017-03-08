package io.skysail.app.instagram.mocked;

import java.util.Arrays;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

import io.skysail.app.instagram.mocked.repositories.InstagramUserRepository;

import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.ApplicationConfiguration;
import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import io.skysail.server.security.config.SecurityConfigBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class InstagramApiMockApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "instagramMock";

    @Reference
    private DbService dbService;

    @Getter
    private InstagramUserRepository repository;

    public InstagramApiMockApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(InstagramUser.class));
        setDescription("mocked instagram API");
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        addRepository(new InstagramUserRepository(dbService));
    }

    protected void defineSecurityConfig(SecurityConfigBuilder securityConfigBuilder) {
        securityConfigBuilder.authorizeRequests().startsWithMatcher("").permitAll();
    }
    
    @Override
    protected void attach() {
    	
    	router.attach(new RouteBuilder("/oauth/authorize/", OAuth2AuthorizeMock.class));
    	router.attach(new RouteBuilder("/oauth/access_token", OAuth2TokenMock.class));
    	
    	router.attach(new RouteBuilder("/users/self", InstagramUserResource.class));
    	router.attach(new RouteBuilder("/users/{id}", InstagramUserResource.class));

    	router.attach(new RouteBuilder("/users/self/media/recent", InstagramUserResource.class));
    	router.attach(new RouteBuilder("/users/{id}/media/recent", InstagramUserResource.class));

    	router.attach(new RouteBuilder("/users/self/media/liked", InstagramUserResource.class));
    	router.attach(new RouteBuilder("/users/search", InstagramUserResource.class));
    }	
}