package io.skysail.app.instagram

import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.Reference
import io.skysail.app.instagram.config.InstagramConfiguration
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
  
  @Reference
  var config: InstagramConfiguration = null

  val log = LoggerFactory.getLogger(classOf[ApiServices])
  
  def getMe(principal: Principal): String = {
    val accessToken = OAuth2Proxy.getAccessToken(principal,InstagramApplication.APP_NAME).get
    val cr = new ClientResource(config.config.apiBaseUrl() + "/users/self?access_token=" + accessToken);
    cr.setMethod(Method.GET);
    try {
      val posted = cr.get(MediaType.APPLICATION_JSON);
      return posted.getText();
    } catch {
      case e: Throwable => log.error(e.getMessage,e)
    }
    ""
  }
  
   def getMeRecentMedia(principal: Principal): String = {
    val accessToken = OAuth2Proxy.getAccessToken(principal,InstagramApplication.APP_NAME).get
    val cr = new ClientResource(config.config.apiBaseUrl() + "/users/self/media/recent/?access_token=" + accessToken);
    cr.setMethod(Method.GET);
    try {
      val posted = cr.get(MediaType.APPLICATION_JSON);
      return posted.getText();
    } catch {
      case _: Throwable => println("error")
    }
    ""
  }
}