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
import io.skysail.app.facebook.FacebookConfiguration

@Component(immediate = true, service = Array(classOf[ApiServices]))
class ApiServices {

  @Reference
  var config: FacebookConfiguration = null

  val log = LoggerFactory.getLogger(classOf[ApiServices])

  def getMe(principal: Principal): String = {
    val accessToken = OAuth2Proxy.getAccessToken(principal, FacebookApplication.APP_NAME).get
    val cr = new ClientResource(config.config.apiBaseUrl() + "/me?access_token=" + accessToken);
    cr.setMethod(Method.GET);
    try {
      val posted = cr.get(MediaType.APPLICATION_JSON);
      return posted.getText();
    } catch {
      case e: Throwable => log.error(e.getMessage, e)
    }
    ""
  }

  def getMePhotos(principal: Principal): String = {
    val accessToken = OAuth2Proxy.getAccessToken(principal, FacebookApplication.APP_NAME).get
    val cr = new ClientResource(config.config.apiBaseUrl() + "/me/photos?access_token=" + accessToken);
    cr.setMethod(Method.GET);
    try {
      val posted = cr.get(MediaType.APPLICATION_JSON);
      return posted.getText();
    } catch {
      case e: Throwable => log.error(e.getMessage, e)
    }
    ""
  }

//  def getMeFriendlists(principal: Principal): String = {
//    val accessToken = OAuth2Proxy.getAccessToken(principal, FacebookApplication.APP_NAME).get
//    val cr = new ClientResource(config.config.apiBaseUrl() + "/me/friendlists?access_token=" + accessToken);
//    cr.setMethod(Method.GET);
//    try {
//      val posted = cr.get(MediaType.APPLICATION_JSON);
//      return posted.getText();
//    } catch {
//      case e: Throwable => log.error(e.getMessage, e)
//    }
//    ""
//  }

  def getMeFriendlists(principal: Principal): String = callApi(principal, "/me/friendlists")
  def getMeFeed(principal: Principal): String = callApi(principal, "/me/feed")

  def callApi(principal: Principal, target: String): String = {
    val accessToken = OAuth2Proxy.getAccessToken(principal, FacebookApplication.APP_NAME).get
    val cr = new ClientResource(config.config.apiBaseUrl() + target + "?access_token=" + accessToken);
    cr.setMethod(Method.GET);
    try {
      val posted = cr.get(MediaType.APPLICATION_JSON);
      return posted.getText();
    } catch {
      case e: Throwable => log.error(e.getMessage, e)
    }
    ""
  }

  //  
  //   def getMeRecentMedia(principal: Principal): String = {
  //    val accessToken = OAuth2Proxy.getAccessToken(principal,InstagramApplication.APP_NAME).get
  //    val cr = new ClientResource(config.config.apiBaseUrl() + "/users/self/media/recent/?access_token=" + accessToken);
  //    cr.setMethod(Method.GET);
  //    try {
  //      val posted = cr.get(MediaType.APPLICATION_JSON);
  //      return posted.getText();
  //    } catch {
  //      case _: Throwable => println("error")
  //    }
  //    ""
  //  }
}