package $basePackageName$;

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

  val log = LoggerFactory.getLogger(classOf[ApiServices])

  def getUser(principal: Principal): String = callApi(principal, "/user");
  def getRepositories(principal: Principal): String = callApi(principal, "/repositories");

  @Reference
  var config: TemplateConfiguration = null

  def callApi(principal: Principal, target: String): String = {
    val accessToken = OAuth2Proxy.getAccessToken(principal, TemplateApplication.APP_NAME).get
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