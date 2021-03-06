package $basePackageName$;

import org.restlet.resource.ClientResource
import org.restlet.data.Reference
import org.restlet.Response
import org.restlet.representation.Representation
import org.restlet.resource.ResourceException
import org.restlet.data.MediaType
import io.skysail.ext.oauth2.domain.OAuth2Parameters
import io.skysail.ext.oauth2.domain.Token
import org.restlet.data.ChallengeScheme
import org.restlet.ext.json.JsonRepresentation
import org.osgi.service.component.annotations.Component
import io.skysail.ext.oauth2.resources.AccessTokenClientResource


@Component(service = Array(classOf[AccessTokenClientResource]))
@Component(service = Array(classOf[AccessTokenClientResource]))
class FacebookAccessTokenClientResource extends AccessTokenClientResource(new Reference("https://template.com/login/oauth/access_token")) {

  def getApiProviderUriMatcher(): String = "template.com"

  def requestToken(parameters: OAuth2Parameters): Token = {
    println(clientId);
    parameters.clientId(clientId)
    parameters.clientSecret(clientSecret)
    val input = parameters.toRepresentation();

    println(input);
    accept(MediaType.APPLICATION_JSON);

    val result = new JsonRepresentation(post(input)).getJsonObject();

    println(result)
    TokenResponse.apply(result);
  }

}