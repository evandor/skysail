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
    val meTaggableFriendsProxy = new OAuth2Proxy(getApplication(), clientParams, serverParams, classOf[FacebookMeTaggableFriendsResource]);
    val meFeedProxy = new OAuth2Proxy(getApplication(), clientParams, serverParams, classOf[FacebookMeFeedResource]);
    val mePostsProxy = new OAuth2Proxy(getApplication(), clientParams, serverParams, classOf[FacebookMePostsResource]);

    router.attach(new RouteBuilder("/api/me", meProxy));
    router.attach(new RouteBuilder("/api/me/photos", mePhotosProxy));
    router.attach(new RouteBuilder("/api/me/friendlists", meFriendlistsProxy));
    router.attach(new RouteBuilder("/api/me/feed", meFeedProxy));
    router.attach(new RouteBuilder("/api/me/posts", mePostsProxy));
    router.attach(new RouteBuilder("/api/me/taggableFriends", meTaggableFriendsProxy));
    
    router.attach(new RouteBuilder("/me/taggableFriends", classOf[ViewerTaggableFriendsResource]));
    
    router.attach(new RouteBuilder("/callback", classOf[OAuth2CallbackResource]));
    
    createStaticDirectory();
  }

  def getFacebookApi(): ApiServices = facebookApi

}