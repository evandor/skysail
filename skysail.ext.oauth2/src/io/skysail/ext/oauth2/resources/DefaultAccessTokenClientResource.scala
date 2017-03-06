package io.skysail.ext.oauth2.resources

import org.restlet.resource.ClientResource
import org.restlet.data.Reference
import org.restlet.Response
import org.restlet.representation.Representation
import org.restlet.resource.ResourceException
import org.restlet.data.MediaType
import io.skysail.ext.oauth2.domain.OAuth2Parameters
import io.skysail.ext.oauth2.domain.Token
import org.restlet.ext.json.JsonRepresentation
import org.restlet.data.ChallengeScheme
import io.skysail.ext.oauth2.domain.TokenResponse
import org.osgi.service.component.annotations.Component

class DefaultAccessTokenClientResource(tokenUri: Reference) extends AccessTokenClientResource(tokenUri) {

  def getApiProviderUriMatcher(): String = "*"

  def requestToken(parameters: OAuth2Parameters): Token = {
    setChallengeResponse(ChallengeScheme.HTTP_BASIC, clientId, clientSecret);
    val input = parameters.toRepresentation();
    accept(MediaType.APPLICATION_JSON);
    val result = new JsonRepresentation(post(input)).getJsonObject();
    //        if (result.has(ERROR)) {
    //            throw OAuthException.toOAuthException(result);
    //        }
    val token = TokenResponse.apply(result);
    //        if (token.scope == null) {
    //            // Should be identical to the scope requested by the client.
    //           // token.scope = Scopes.parseScope(parameters.toForm().getFirstValue(SCOPE));
    //        }

    return token;
  }

}