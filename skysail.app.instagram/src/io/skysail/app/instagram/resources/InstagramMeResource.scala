package io.skysail.app.instagram

import io.skysail.server.restlet.resources.EntityServerResource
import io.skysail.domain.GenericIdentifiable

class InstagramMeResource extends EntityServerResource[GenericIdentifiable] {
  def getEntity(): GenericIdentifiable = {
    val app = getApplication().asInstanceOf[InstagramApplication];
    val me = app.getInstagramApi().getMe(getPrincipal());
    new GenericIdentifiable(me);
  }
}
