package io.skysail.app.github.resources

import org.restlet.data.Reference
import org.restlet.data.MediaType
import io.skysail.ext.oauth2.domain.OAuth2Parameters
import io.skysail.ext.oauth2.domain.Token
import org.restlet.ext.json.JsonRepresentation
import org.osgi.service.component.annotations.Component
import io.skysail.ext.oauth2.resources.AccessTokenClientResource
import io.skysail.app.github.TokenResponse


@Component(service = Array(classOf[AccessTokenClientResource]))
@Component(service = Array(classOf[AccessTokenClientResource]))
class FacebookAccessTokenClientResource extends AccessTokenClientResource(new Reference("https://github.com/login/oauth/access_token")) {

  def getApiProviderUriMatcher(): String = "github.com"

  def requestToken(parameters: OAuth2Parameters): Token = {
    println(clientId);
    parameters.clientId(clientId)
    parameters.clientSecret(clientSecret)
    val input = parameters.toRepresentation();

    println(input);
    accept(MediaType.APPLICATION_JSON);

    val result = new JsonRepresentation(post(input)).getJsonObject();

    println(result)
    val token = TokenResponse.apply(result);

    return token;
  }

}