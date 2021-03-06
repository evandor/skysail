package io.skysail.app.instagram

import io.skysail.server.restlet.resources.EntityServerResource
import io.skysail.domain.GenericIdentifiable
import org.slf4j.LoggerFactory

class OAuth2CallbackResource extends EntityServerResource[GenericIdentifiable] {
  
  val log = LoggerFactory.getLogger(classOf[OAuth2CallbackResource])
  
  def getEntity(): GenericIdentifiable = {
    val storedState= getContext().getAttributes().get(InstagramApplication.INSTAGRAM_AUTH_STATE).asInstanceOf[String];
//        if (state == null || !state.equals(storedState)) {
//            log.warn("state does not match when logging in to spotify, redirecting to root");
////            Reference redirectTo = new Reference("/");
////            getResponse().redirectSeeOther(redirectTo);
////            return null;
//        }
//
        getContext().getAttributes().remove(InstagramApplication.INSTAGRAM_AUTH_STATE);
        //String callbackJson = application.getSpotifyApi().getToken(code);
        //ApiServices.setAccessData(getPrincipal(), callbackJson);
        var target = getContext().getAttributes().get("oauthTarget").asInstanceOf[String]
        target += "?code=" + getQueryValue("code") +"&state=" + getQueryValue("state");
        log.info("redirecting to '{}'", target);
        getResponse().redirectTemporary(target);
        return null;
  }
}