package io.skysail.app.spotify;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.restlet.data.Protocol;

import io.skysail.app.spotify.config.SpotifyConfigDescriptor;
import io.skysail.app.spotify.config.SpotifyConfiguration;
import io.skysail.app.spotify.resources.SpotifyLoginCallback2;
import io.skysail.app.spotify.resources.SpotifyMePlaylistsResource3;
import io.skysail.app.spotify.resources.SpotifyMeResource;
import io.skysail.app.spotify.resources.SpotifyRootResource;
import io.skysail.app.spotify.services.ApiServices;
import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.ApplicationConfiguration;
import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.ext.oauth2.OAuth2ClientParameters;
import io.skysail.ext.oauth2.OAuth2Proxy;
import io.skysail.ext.oauth2.OAuth2ServerParameters;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class SpotifyApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "spotify";
    public static final String SPOTIFY_AUTH_STATE = "spotify_auth_state";

    @Reference
    @Getter
    private SpotifyConfiguration config;

    @Reference
    @Getter
    ApiServices spotifyApi;

    public SpotifyApplication() {
        super(APP_NAME, new ApiVersion(1));
        setDescription("a skysail application");
        getConnectorService().getClientProtocols().add(Protocol.HTTPS);
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
    }

    @Override
    protected void attach() {
        router.attach(new RouteBuilder("", SpotifyRootResource.class));
        router.attach(new RouteBuilder("/", SpotifyRootResource.class));
        router.attach(new RouteBuilder("/me", SpotifyMeResource.class));

        SpotifyConfigDescriptor c = config.getConfig();
        OAuth2ClientParameters clientParams = new OAuth2ClientParameters(c.clientId(), c.clientSecret(),c.scope(),
                c.redirectUri());

        OAuth2ServerParameters serverParams = new OAuth2ServerParameters(c.apiBaseUrl(),c.authBaseUrl(),c.authUri(),c.tokenUri());

        OAuth2Proxy oAuth2Proxy = new OAuth2Proxy(getApplication(), clientParams, serverParams,SpotifyMePlaylistsResource3.class);

        router.attach(new RouteBuilder("/me/playlists", oAuth2Proxy));
        router.attach(new RouteBuilder("/callback", SpotifyLoginCallback2.class));
        createStaticDirectory();
    }

}