package io.skysail.app.spotify;

import java.util.Arrays;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.restlet.data.Protocol;

import io.skysail.app.spotify.repositories.AggregateRootEntityRepository;
import io.skysail.app.spotify.resources.SpotifyLogin;
import io.skysail.app.spotify.resources.SpotifyLoginCallback;
import io.skysail.core.app.ApiVersion;
import io.skysail.core.app.ApplicationConfiguration;
import io.skysail.core.app.ApplicationProvider;
import io.skysail.core.app.SkysailApplication;
import io.skysail.server.db.DbService;
import io.skysail.server.menus.MenuItemProvider;
import io.skysail.server.restlet.RouteBuilder;
import lombok.Getter;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class TemplateApplication extends SkysailApplication implements ApplicationProvider, MenuItemProvider {

    public static final String APP_NAME = "spotify";

    public static final String SPOTIFY_AUTH_STATE = "spotify_auth_state";

    @Getter
    private String apiBase = "http://accounts.spotify.com";

    @Getter
    private String clientId = "1ec49b84a6cf49149073665f3327adb7";

    @Getter
    private String clientSecret = "b4bce2a9a9f848c88c8ca160a8668ff9";

    @Getter
    private String redirectUri = "http://localhost:2021/"+APP_NAME+"/v1/callback";

    @Reference
    private DbService dbService;

    @Getter
    private AggregateRootEntityRepository repository;

    public TemplateApplication() {
        super(APP_NAME, new ApiVersion(1), Arrays.asList(AggregateRootEntity.class));
        setDescription("a skysail application");
        getConnectorService().getClientProtocols().add(Protocol.HTTPS);
    }

    @Activate
    @Override
    public void activate(ApplicationConfiguration appConfig, ComponentContext componentContext)
            throws ConfigurationException {
        super.activate(appConfig, componentContext);
        addRepository(new AggregateRootEntityRepository(dbService));
    }

    @Override
    protected void attach() {
        super.attach();

        router.attach(new RouteBuilder("/login", SpotifyLogin.class));
        router.attach(new RouteBuilder("/callback", SpotifyLoginCallback.class));
    }

}