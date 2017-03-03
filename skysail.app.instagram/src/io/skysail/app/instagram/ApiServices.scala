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

@Component(immediate = true, service = Array(classOf[ApiServices]))
class ApiServices {

  def getMe(principal: Principal): String = {
    val accessToken = OAuth2Proxy.getAccessToken(principal).get
    val cr = new ClientResource("https://api.instagram.com/v1/users/self/?access_token=" + accessToken);
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