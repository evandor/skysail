package io.skysail.app.facebook.resources

import io.skysail.server.restlet.resources.EntityServerResource
import io.skysail.domain.GenericIdentifiable
import io.skysail.app.facebook.FacebookApplication

class FacebookMeFriendlistsResource extends EntityServerResource[GenericIdentifiable] {
  def getEntity(): GenericIdentifiable = {
    val app = getApplication().asInstanceOf[FacebookApplication];
    val me = app.getFacebookApi().getMeFriendlists(getPrincipal());
    new GenericIdentifiable(me);
  }
}
