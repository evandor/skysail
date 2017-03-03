package io.skysail.ext.oauth2

import org.restlet.routing.Filter
import org.restlet.Request
import org.restlet.Response

import io.skysail.ext.oauth2.domain.Token
import io.skysail.ext.oauth2.resources.DefaultAccessTokenClientResource
import io.skysail.ext.oauth2.domain.OAuth2Parameters
import io.skysail.ext.oauth2.domain.ResponseType
import io.skysail.ext.oauth2.domain.GrantType
import io.skysail.server.utils.LinkUtils
import org.restlet.routing.Filter._
import org.restlet.data.Form
import org.restlet.data.Reference
import org.restlet.resource.ServerResource
import org.restlet.Context
import org.slf4j.LoggerFactory
import io.skysail.core.resources.SkysailServerResource
import io.skysail.core.app.SkysailApplication
import io.skysail.api.links.Link
import io.skysail.server.utils.LinkUtils
import java.security.Principal
import io.skysail.ext.oauth2.resources.AccessTokenClientResource

object OAuth2Proxy { //extends AccessTokenClientResourceProvider {
  val tokens = collection.mutable.Map[String, Token]()
  def getAccessToken(principal: Principal): Option[String] = {
    val optionalToken = tokens.get(principal.getName)
    if (optionalToken.isDefined) {
      Some(optionalToken.get.accessToken);
    } else {
      None
    }
  }

//  def get(): AccessTokenClientResource = {
//    new DefaultAccessTokenClientResource(new Reference(serverParams.tokenUri), clientParams.clientId, clientParams.clientSecret);
//  }
}

class OAuth2Proxy(
    application: SkysailApplication,
    clientParams: OAuth2ClientParameters,
    serverParams: OAuth2ServerParameters,
    next: Class[_ <: SkysailServerResource[_]]) extends Filter {

  val log = LoggerFactory.getLogger(classOf[OAuth2Proxy])

  setNext(next);

  override def beforeHandle(request: Request, response: Response): Int = {
    //request.setCacheDirectives(no);
    val params = new Form(request.getOriginalRef().getQuery());

    try {
      val error = params.getFirstValue("error");
      if (notNullOrEmpty(error)) {
        //validateState(request, params); // CSRF protection
        //return sendErrorPage(response, OAuthException.toOAuthException(params));
      }

      val code = params getFirstValue "code"
      if (notNullOrEmpty(code)) {
        // Execute authorization_code grant
        //validateState(request, params); // CSRF protection
        val token = requestToken(code);
        request.getAttributes().put(classOf[Token].getName, token);
        OAuth2Proxy.tokens.put("admin", token)
        return CONTINUE;
      }
    } catch {
      case e: Throwable => e.printStackTrace()
      //return sendErrorPage(response, ex);
    }

    val authRequestParameter = createAuthorizationRequestParameter();
    //authRequest.state(setupState(response)); // CSRF protection
    val redirRef = authRequestParameter.toReference(serverParams.authUri);
    //     response.setCacheDirectives(no);
    val targetLink = LinkUtils.fromResource(application, next);
    log.info("authRequest to '{}' with targetUri '{" + targetLink.getUri() + "}'", redirRef);
    application.getContext().getAttributes.put("oauthTarget", targetLink.getUri());
    response.redirectTemporary(redirRef);
    return STOP
  }

  def createAuthorizationRequestParameter(): OAuth2Parameters = {
    val parameters = new OAuth2Parameters()
      .responseType(ResponseType.CODE)
      .add("client_id", clientParams.clientId);
    if (clientParams.redirectUri != null) {
      parameters.redirectUri(clientParams.redirectUri);
    }
    if (clientParams.scope != null) {
      parameters.scope(clientParams.scope);
    }
    return parameters;
  }

  private def requestToken(code: String): Token = {

    // var  tokenResource: AccessTokenClientResource;
    /*    if (endpoint.contains("graph.facebook.com")) {
            // We should use Facebook implementation. (Old draft spec.)
            tokenResource = new FacebookAccessTokenClientResource(new Reference(endpoint));
        } else {*/
    val tokenResource = new DefaultAccessTokenClientResource(new Reference(serverParams.tokenUri), clientParams.clientId, clientParams.clientSecret);
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

    if (clientParams.redirectUri != null) {
      parameters.redirectUri(clientParams.redirectUri);
    }
    return parameters;
  }

  private def notNullOrEmpty(value: String) = value != null && value.length() > 0
}
