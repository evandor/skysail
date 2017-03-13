package io.skysail.app.facebook.resources

import io.skysail.server.restlet.resources.EntityServerResource
import io.skysail.domain.GenericIdentifiable
import io.skysail.app.facebook.FacebookApplication
import java.security.Principal

abstract class FacebookResource extends EntityServerResource[GenericIdentifiable] {

  var app = getApplication().asInstanceOf[FacebookApplication];
  
  def apiCall (principal: Principal):String
  
  def getEntity(): GenericIdentifiable = {
    val app = getApplication().asInstanceOf[FacebookApplication];
    val me = app.getFacebookApi().getMe(getPrincipal());
    new GenericIdentifiable(apiCall(getPrincipal()))
  }

}

  
