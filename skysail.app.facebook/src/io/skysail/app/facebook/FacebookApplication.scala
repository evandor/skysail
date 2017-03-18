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
import io.skysail.app.facebook.resources.FacebookMeFeedResource
import io.skysail.app.facebook.resources.FacebookMeResource
import io.skysail.app.facebook.resources.FacebookMePhotosResource
import io.skysail.app.facebook.resources.FacebookMeFriendlistsResource
import io.skysail.app.facebook.resources.FacebookMeTaggableFriendsResource
import io.skysail.app.facebook.resources.ViewerTaggableFriendsResource
import io.skysail.app.facebook.resources.FacebookMePostsResource
import io.skysail.core.resources.SkysailServerResource
import io.skysail.app.facebook.resources.ViewerMeResource

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
    new ApiVersion(int2Integer(1))) with MenuItemProvider {

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

    router.attach(new RouteBuilder("", classOf[ViewerMeResource]));
    
    router.attach(new RouteBuilder("/me", classOf[ViewerMeResource]));
    router.attach(new RouteBuilder("/me/taggableFriends", classOf[ViewerTaggableFriendsResource]));

    router.attach(new RouteBuilder("/api/me", oauth2ProxyFor(classOf[FacebookMeResource])));
    router.attach(new RouteBuilder("/api/me/photos", oauth2ProxyFor(classOf[FacebookMePhotosResource])));
    router.attach(new RouteBuilder("/api/me/friendlists", oauth2ProxyFor(classOf[FacebookMeFriendlistsResource])));
    router.attach(new RouteBuilder("/api/me/feed", oauth2ProxyFor(classOf[FacebookMeFeedResource])));
    router.attach(new RouteBuilder("/api/me/posts", oauth2ProxyFor(classOf[FacebookMePostsResource])));
    router.attach(new RouteBuilder("/api/me/taggableFriends", oauth2ProxyFor(classOf[FacebookMeTaggableFriendsResource])));
    
    router.attach(new RouteBuilder("/callback", classOf[OAuth2CallbackResource]));
    
    createStaticDirectory();
  }

  def oauth2ProxyFor(cls: Class[_ <: SkysailServerResource[_]]): OAuth2Proxy = {
    val clientParams = new OAuth2ClientParameters(config.config);
    val serverParams = new OAuth2ServerParameters(config.config);
    new OAuth2Proxy(getApplication(), clientParams, serverParams, cls);
  }

  def getFacebookApi(): ApiServices = facebookApi

}