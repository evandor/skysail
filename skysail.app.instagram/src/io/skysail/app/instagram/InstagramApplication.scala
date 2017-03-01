package io.skysail.app.instagram

import io.skysail.core.app.SkysailApplication
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import io.skysail.core.app.ApplicationProvider
import io.skysail.server.menus.MenuItemProvider
import io.skysail.core.app.ApiVersion
import org.osgi.service.component.annotations.Activate
import org.osgi.service.component.ComponentContext
import io.skysail.core.app.ApplicationConfiguration
import org.restlet.data.Protocol
import io.skysail.server.restlet.RouteBuilder
import org.restlet.data.Reference
import io.skysail.app.instagram.config.InstagramConfiguration
import io.skysail.ext.oauth2.config.OAuth2ConfigDescriptor
import io.skysail.ext.oauth2.OAuth2Proxy
import io.skysail.ext.oauth2.OAuth2ClientParameters
import io.skysail.ext.oauth2.OAuth2ServerParameters

object InstagramApplication {
  final val APP_NAME = "instagram"
  final val INSTAGRAM_AUTH_STATE = "instagram_auth_state"
}

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL)
class InstagramApplication extends SkysailApplication(InstagramApplication.APP_NAME, new ApiVersion(1)) with ApplicationProvider with MenuItemProvider {

  setDescription("instagram client")
  getConnectorService().getClientProtocols().add(Protocol.HTTPS)
  
  @Reference
  var config: InstagramConfiguration = null

  @Activate
  override def activate(appConfig: ApplicationConfiguration, componentContext: ComponentContext) = {
    super.activate(appConfig, componentContext);
  }
  
  override def attach() {
        val c = config.config
        val clientParams = new OAuth2ClientParameters(
                c.clientId(),
                c.clientSecret(),
                c.redirectUri());

        val serverParams = new OAuth2ServerParameters(
            "https://accounts.spotify.com/authorize",
            "https://accounts.spotify.com/api/token"
        );

        val oAuth2Proxy = new OAuth2Proxy(getContext(), clientParams,serverParams, classOf[InstagramMeResource]);
//
        router.attach(new RouteBuilder("/me", oAuth2Proxy));
        router.attach(new RouteBuilder("/callback", classOf[OAuth2CallbackResource]));
        createStaticDirectory();
    }
}