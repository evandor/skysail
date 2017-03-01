package io.skysail.ext.oauth2

import org.restlet.routing.Filter
import org.restlet.Request
import org.restlet.Response

import org.restlet.routing.Filter._
import org.restlet.data.Form
import io.skysail.ext.oauth2.domain.Token
import io.skysail.ext.oauth2.resources.AccessTokenClientResource
import org.restlet.data.Reference
import io.skysail.ext.oauth2.domain.OAuth2Parameters
import io.skysail.ext.oauth2.domain.GrantType
import org.restlet.resource.ServerResource
import io.skysail.ext.oauth2.domain.ResponseType
import org.restlet.Context
import lombok.extern.slf4j.Slf4j
import org.slf4j.LoggerFactory

@Slf4j
class OAuth2Proxy(
    context: Context, 
    clientParams: OAuth2ClientParameters, 
    serverParams: OAuth2ServerParameters, 
    next: Class[_ <: ServerResource]) extends Filter {

  val log = LoggerFactory.getLogger(classOf[OAuth2Proxy])
  
  setNext(next);
  
  override def beforeHandle(request: Request, response: Response): Int = {
    //request.setCacheDirectives(no);
    val params = new Form(request.getOriginalRef().getQuery());

    try {
      // Check if error is available.
      val error = params.getFirstValue("error");
      if (notNullOrEmpty(error)) {
        //validateState(request, params); // CSRF protection
        //return sendErrorPage(response, OAuthException.toOAuthException(params));
      }
      // Check if code is available.
      val code = params.getFirstValue("code");
      if (notNullOrEmpty(code)) {
        // Execute authorization_code grant
        //validateState(request, params); // CSRF protection
        val token = requestToken(code);
        request.getAttributes().put(classOf[Token].getName, token);
        return CONTINUE;
      }
    } catch {
      case e: Throwable => e.printStackTrace()
      //return sendErrorPage(response, ex);
    }
    val authRequest = createAuthorizationRequest();
    //authRequest.state(setupState(response)); // CSRF protection
    val redirRef = authRequest.toReference(serverParams.getAuthUri());
    //     response.setCacheDirectives(no);
    log.info("redirecting to '{}'", redirRef);
    context.getAttributes.put("oauthTarget", "/spotify/v1/me/playlists3");
    response.redirectTemporary(redirRef);
    return STOP
  }

  def createAuthorizationRequest(): OAuth2Parameters = {
    val parameters = new OAuth2Parameters()
      .responseType(ResponseType.CODE)
      .add("client_id", clientParams.getClientId());
    if (clientParams.getRedirectUri() != null) {
      parameters.redirectUri(clientParams.getRedirectUri());
    }
//    if (scope != null) {
//     // parameters.scope(scope);
//    }
    return parameters;
  }

  private def requestToken(code: String): Token = {

    // var  tokenResource: AccessTokenClientResource;
    val endpoint = serverParams.getTokenUri();
    /*    if (endpoint.contains("graph.facebook.com")) {
            // We should use Facebook implementation. (Old draft spec.)
            tokenResource = new FacebookAccessTokenClientResource(new Reference(endpoint));
        } else {*/
    val tokenResource = new AccessTokenClientResource(new Reference(endpoint),clientParams.getClientId(),clientParams.getClientSecret());
    //tokenResource.setAuthenticationMethod(basicSecret ? ChallengeScheme.HTTP_BASIC : null);
    /* }*/

    //tokenResource.setClientCredentials(clientParams.getClientId(), clientParams.getClientSecret());

    //tokenResource.setNext(next);

    val tokenRequest = createTokenRequest(code);

    try {
      return tokenResource.requestToken(tokenRequest);
    } finally {
      tokenResource.release();
    }
  }

  def createTokenRequest(code: String): OAuth2Parameters = {
    val parameters = new OAuth2Parameters()
      .grantType(GrantType.AUTHORIZATION_CODE)
      .code(code);

    if (clientParams.getRedirectUri() != null) {
      parameters.redirectUri(clientParams.getRedirectUri());
    }
    return parameters;
  }

  private def notNullOrEmpty(value: String) = value != null && value.length() > 0
}
