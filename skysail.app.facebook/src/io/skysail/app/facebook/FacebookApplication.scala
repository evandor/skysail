package io.skysail.app.facebook

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
import io.skysail.ext.oauth2.OAuth2Proxy
import io.skysail.ext.oauth2.OAuth2ClientParameters
import io.skysail.ext.oauth2.OAuth2ServerParameters
import io.skysail.server.menus.MenuItem
import java.util.Arrays

object FacebookApplication {
  final val APP_NAME = "facebook"
  final val FACEBOOK_AUTH_STATE = "facebook_auth_state"
}

@Component(
  immediate = true, 
  configurationPolicy = ConfigurationPolicy.OPTIONAL, 
  service = Array(classOf[ApplicationProvider], classOf[MenuItemProvider])
)
class FacebookApplication extends SkysailApplication(
    FacebookApplication.APP_NAME, 
    new ApiVersion(int2Integer(1))//,
    //Arrays.asList(classOf[InstagramUser],classOf[InstagramMedia])
) with MenuItemProvider {

  setDescription("facebook client")
  getConnectorService().getClientProtocols().add(Protocol.HTTPS)

  @Reference
  var config: FacebookConfiguration = null

  @Reference
  var facebookApi: ApiServices = null

  @Activate
  override def activate(appConfig: ApplicationConfiguration, componentContext: ComponentContext) = {
    super.activate(appConfig, componentContext);
  }

  override def attach() = {
    val clientParams = new OAuth2ClientParameters(config.config);
    val serverParams = new OAuth2ServerParameters(config.config);
    val meProxy = new OAuth2Proxy(getApplication(), clientParams, serverParams, classOf[FacebookMeResource]);
    val mePhotosProxy = new OAuth2Proxy(getApplication(), clientParams, serverParams, classOf[FacebookMePhotosResource]);
    val meFriendlistsProxy = new OAuth2Proxy(getApplication(), clientParams, serverParams, classOf[FacebookMeFriendlistsResource]);
    val meFeedProxy = new OAuth2Proxy(getApplication(), clientParams, serverParams, classOf[FacebookMeFeedResource]);
//    val selfProxy = new OAuth2Proxy(getApplication(), clientParams, serverParams, classOf[SelfResource]);
//    val meRecentProxy = new OAuth2Proxy(getApplication(), clientParams, serverParams, classOf[MeRecentResource]);
//
    router.attach(new RouteBuilder("/me", meProxy));
    router.attach(new RouteBuilder("/me/photos", mePhotosProxy));
    router.attach(new RouteBuilder("/me/friendlists", meFriendlistsProxy));
    router.attach(new RouteBuilder("/me/feed", meFeedProxy));
//    router.attach(new RouteBuilder("/users/self", selfProxy));
//    router.attach(new RouteBuilder("/users/self/media/recent", meRecentProxy));
    router.attach(new RouteBuilder("/callback", classOf[OAuth2CallbackResource]));
    
    createStaticDirectory();
  }

  def getFacebookApi(): ApiServices = facebookApi

}