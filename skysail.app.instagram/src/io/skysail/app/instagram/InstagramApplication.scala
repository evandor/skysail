package io.skysail.app.instagram

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.restlet.data.Protocol;
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
import io.skysail.app.instagram.config.InstagramConfiguration
import io.skysail.ext.oauth2.config.OAuth2ConfigDescriptor
import io.skysail.ext.oauth2.OAuth2Proxy
import io.skysail.ext.oauth2.OAuth2ClientParameters
import io.skysail.ext.oauth2.OAuth2ServerParameters
import io.skysail.server.menus.MenuItem
import java.util.Arrays

object InstagramApplication {
  final val APP_NAME = "instagram"
  final val INSTAGRAM_AUTH_STATE = "instagram_auth_state"
}

@Component(
    immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL, service = Array(classOf[ApplicationProvider],classOf[MenuItemProvider]))
class InstagramApplication extends SkysailApplication(InstagramApplication.APP_NAME, new ApiVersion(int2Integer(1))) with MenuItemProvider {

  setDescription("instagram client")
  getConnectorService().getClientProtocols().add(Protocol.HTTPS)

  @Reference
  var config: InstagramConfiguration = null

  @Activate
  override def activate(appConfig: ApplicationConfiguration, componentContext: ComponentContext) = {
    super.activate(appConfig, componentContext);
  }
  
  override def attach() = {
    val c = config.config
    val clientParams = new OAuth2ClientParameters(
      c.clientId(),
      c.clientSecret(),
      c.scope(),
      c.redirectUri());

    val serverParams = new OAuth2ServerParameters(
      "https://api.instagram.com/oauth/authorize/",
      "https://api.instagram.com/oauth/access_token");

    val oAuth2Proxy = new OAuth2Proxy(getApplication(), clientParams, serverParams, classOf[InstagramMeResource]);
    //
    router.attach(new RouteBuilder("/me", oAuth2Proxy));
    router.attach(new RouteBuilder("/callback", classOf[OAuth2CallbackResource]));
    createStaticDirectory();
  }
}