package io.skysail.app.facebook

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
import io.skysail.ext.oauth2.domain.TokenResponse


@Component(service = Array(classOf[AccessTokenClientResource]))
@Component(service = Array(classOf[AccessTokenClientResource]))
class FacebookAccessTokenClientResource extends AccessTokenClientResource(new Reference("https://graph.facebook.com/v2.8/oauth/access_token")) {

  def getApiProviderUriMatcher(): String = "www.facebook.com"

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