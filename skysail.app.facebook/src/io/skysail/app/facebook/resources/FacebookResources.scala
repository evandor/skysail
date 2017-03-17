package io.skysail.app.facebook.resources

import io.skysail.server.restlet.resources.EntityServerResource
import io.skysail.domain.GenericIdentifiable
import io.skysail.app.facebook.FacebookApplication
import java.security.Principal
import java.util.Arrays

abstract class FacebookResource extends EntityServerResource[GenericIdentifiable] {

  var app = getApplication().asInstanceOf[FacebookApplication];
  def apiCall(principal: Principal): String

  def getEntity(): GenericIdentifiable = {
    val app = getApplication().asInstanceOf[FacebookApplication];
    val me = app.getFacebookApi().getMe(getPrincipal());
    new GenericIdentifiable(apiCall(getPrincipal()))
  }
}

class FacebookMeTaggableFriendsResource extends FacebookResource {
  def apiCall(principal: Principal): String = app.getFacebookApi().getMeTaggableFriends(getPrincipal())
  override def getPolymerUiExtensions(): java.util.List[String] =
    Arrays.asList("sky-content-friends")
}

class FacebookMePostsResource extends EntityServerResource[GenericIdentifiable] {
  var app = getApplication().asInstanceOf[FacebookApplication];
  def apiCall(principal: Principal): String = app.getFacebookApi().getMePosts(getPrincipal())
  def getEntity(): GenericIdentifiable = {
    val app = getApplication().asInstanceOf[FacebookApplication];
    val me = app.getFacebookApi().getMe(getPrincipal());
    new GenericIdentifiable(apiCall(getPrincipal()))
  }
  override def getPolymerUiExtensions(): java.util.List[String] = Arrays.asList("sky-content-posts")
}
