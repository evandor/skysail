package io.skysail.app.facebook

import io.skysail.server.restlet.resources.EntityServerResource
import io.skysail.domain.GenericIdentifiable

class FacebookMeResource extends EntityServerResource[GenericIdentifiable] {
  def getEntity(): GenericIdentifiable = {
    val app = getApplication().asInstanceOf[FacebookApplication];
    val me = app.getFacebookApi().getMe(getPrincipal());
    new GenericIdentifiable(me);
  }
}
