package io.skysail.app.facebook

import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.Reference
import org.osgi.service.component.annotations.ReferenceCardinality
import java.security.Principal
import org.restlet.resource.ClientResource
import io.skysail.ext.oauth2.OAuth2Proxy
import org.restlet.representation.Representation
import org.restlet.data.MediaType
import org.restlet.data.Method
import org.slf4j.LoggerFactory

@Component(immediate = true, service = Array(classOf[ApiServices]))
class ApiServices {

  def getMe(principal: Principal): String = callApi(principal, "/me");
  def getMePhotos(principal: Principal): String = callApi(principal, "/me/photos");
  def getMePosts(principal: Principal): String = callApi(principal, "/me/posts");
  def getMeFriendlists(principal: Principal): String = callApi(principal, "/me/friendlists")
  def getMeFeed(principal: Principal): String = callApi(principal, "/me/feed?fields=message,created_time,picture")
  def getMeTaggableFriends(principal: Principal): String = callApi(principal, "/me/taggable_friends")

  val log = LoggerFactory.getLogger(classOf[ApiServices])

  @Reference
  var config: FacebookConfiguration = null

  def callApi(principal: Principal, target: String): String = {
    val accessToken = OAuth2Proxy.getAccessToken(principal, FacebookApplication.APP_NAME).get
    val cr = new ClientResource(config.config.apiBaseUrl() + target + divider(target) + "access_token=" + accessToken);
    cr.setMethod(Method.GET);
    try {
      val posted = cr.get(MediaType.APPLICATION_JSON);
      return posted.getText();
    } catch {
      case e: Throwable => log.error(e.getMessage, e)
    }
    ""
  }

  def divider(target: String):String = {
    if (target.contains("?")) {
      return "&"
    }
    return "?"
  }
}