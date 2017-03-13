package io.skysail.app.instagram.resources

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
class InstagramAccessTokenClientResource extends AccessTokenClientResource(new Reference("https://api.instagram.com/oauth/access_token")) {

  def getApiProviderUriMatcher(): String = "api.instagram.com"

  def requestToken(parameters: OAuth2Parameters): Token = {
    println(clientId);
    //setChallengeResponse(ChallengeScheme.HTTP_BASIC, clientId, clientSecret);
    parameters.clientId(clientId)
    parameters.clientSecret(clientSecret)
    val input = parameters.toRepresentation();

    println(input);
    accept(MediaType.APPLICATION_JSON);

    val result = new JsonRepresentation(post(input)).getJsonObject();

    val token = TokenResponse.apply(result);

    return token;
  }

}