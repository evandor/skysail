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

//@Component(service = Array(classOf[AccessTokenClientResource]), property = Array("apiProviderUrlMatch=*"))
class DefaultAccessTokenClientResource(
    tokenUri: Reference,
    clientId: String,
    clientSecret: String) extends AccessTokenClientResource(tokenUri) {

   //  def setClientCredentials(clientId: String, clientSecret: String): Unit = {
  //    this.clientId = clientId;
  //    this.clientSecret = clientSecret;
  //  }

  def requestToken(parameters: OAuth2Parameters): Token = {
    //        if (authenticationScheme == null) {
    //            // Use Body method
    //            setupBodyClientCredentials(parameters);
    //        } else {
    println(clientId);
    setChallengeResponse(ChallengeScheme.HTTP_BASIC, clientId, clientSecret);
    //        }

    val input = parameters.toRepresentation();

    println(input);
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