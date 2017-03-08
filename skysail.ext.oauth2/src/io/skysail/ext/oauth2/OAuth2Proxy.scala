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
import io.skysail.ext.oauth2.components.AccessTokenClientResourceCollector
import java.util.ArrayList
import org.restlet.data.CacheDirective

object OAuth2Proxy {
  val noCache = new ArrayList[CacheDirective]();
  val tokens = collection.mutable.Map[String, Token]()
  def getAccessToken(principal: Principal, apiProviderUri: String): Option[String] = {
    //tokens.get(principal.getName).map { t => t.accessToken }
    val optionalToken = tokens.get(principal.getName + "-" + apiProviderUri)
    if (optionalToken.isDefined) {
      Some(optionalToken.get.accessToken);
    } else {
      None
    }
  }
}

class OAuth2Proxy(
    application: SkysailApplication, 
    clientParams: OAuth2ClientParameters, 
    serverParams: OAuth2ServerParameters, 
    next: Class[_ <: SkysailServerResource[_]]) extends Filter {

  val log = LoggerFactory.getLogger(classOf[OAuth2Proxy])
  val defaultClientResource = setUpDefaultClientResource

  setNext(next)

  override def beforeHandle(request: Request, response: Response): Int = {
    request.setCacheDirectives(OAuth2Proxy.noCache);
    val params = new Form(request.getOriginalRef().getQuery());

    try {
      val error = params.getFirstValue("error");
      if (notNullOrEmpty(error)) {
        //validateState(request, params); // CSRF protection
        //return sendErrorPage(response, OAuthException.toOAuthException(params));
      }
      
      val optionalToken = OAuth2Proxy.tokens.get(tokenIdentifierFor(request, serverParams.authUri))
      if (optionalToken.isDefined) {
        return CONTINUE
      }

      val code = params getFirstValue "code"
      if (notNullOrEmpty(code)) {
        // Execute authorization_code grant
        //validateState(request, params); // CSRF protection
        val token = requestToken(code);
        //request.getAttributes().put(classOf[Token].getName, token);
        OAuth2Proxy.tokens.put(tokenIdentifierFor(request, application.getName()), token)
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
    val tokenResource = determineAccessTokenClientResource();
    tokenResource.setClientId(clientParams.clientId)
    tokenResource.setClientSecret(clientParams.clientSecret)
    val tokenRequest = createTokenRequestParameter(code);
    try {
      return tokenResource.requestToken(tokenRequest);
    } finally {
      tokenResource.release();
    }
  }

  def createTokenRequestParameter(code: String): OAuth2Parameters = {
    val parameters = new OAuth2Parameters()
      .grantType(GrantType.AUTHORIZATION_CODE)
      .code(code);

    if (clientParams.redirectUri != null) {
      parameters.redirectUri(clientParams.redirectUri);
    }
    return parameters;
  }

  private def notNullOrEmpty(value: String) = value != null && value.length() > 0

  private def determineAccessTokenClientResource(): AccessTokenClientResource = {
    AccessTokenClientResourceCollector.elements
      .find { r => serverParams.tokenUri.contains(r.getApiProviderUriMatcher()) }
      .orElse(Some(defaultClientResource))
      .get
  }

  private def setUpDefaultClientResource(): AccessTokenClientResource = {
    val resource = new DefaultAccessTokenClientResource(new Reference(serverParams.tokenUri))
    resource.setClientId(clientParams.clientId)
    resource.setClientSecret(clientParams.clientSecret)
    resource
  }

  def tokenIdentifierFor(request: Request, apiProviderIdent: String): String = {
    application.getAuthenticationService().getPrincipal(request).getName + "-" + apiProviderIdent
  }
}
