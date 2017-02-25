package io.skysail.app.spotify;

import java.util.Arrays;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.restlet.data.Protocol;

import io.skysail.app.spotify.config.SpotifyConfiguration;
import io.skysail.app.spotify.repositories.AggregateRootEntityRepository;
import io.skysail.app.spotify.resources.SpotifyLogin;
import io.skysail.app.spotify.resources.SpotifyLoginCallback;
import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.ApplicationConfiguration;
import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class SpotifyApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "spotify";

    public static final String SPOTIFY_AUTH_STATE = "spotify_auth_state";

    @Getter
    private String apiBase = "https://accounts.spotify.com";

    @Reference
    @Getter
    private SpotifyConfiguration config;

    @Getter
    private AggregateRootEntityRepository repository;

    public SpotifyApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(AggregateRootEntity.class));
        setDescription("a skysail application");
        getConnectorService().getClientProtocols().add(Protocol.HTTPS);
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        //addRepository(new AggregateRootEntityRepository(dbService));
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("/login", SpotifyLogin.class));
        router.attach(new RouteBuilder("/callback", SpotifyLoginCallback.class));
    }

    public String getSpotifyRedirectUri() {
        return getConfig().getConfig().redirectUri();
    }
    public String getSpotifyClientId() {
        return getConfig().getConfig().clientId();
    }
    public String getSpotifyClientSecret() {
        return getConfig().getConfig().clientSecret();
    }

}