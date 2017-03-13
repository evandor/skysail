package io.skysail.app.facebook.resources

import io.skysail.server.restlet.resources.EntityServerResource
import io.skysail.domain.GenericIdentifiable
import io.skysail.app.facebook.FacebookApplication
import java.security.Principal

class FacebookMePhotosResource extends FacebookResource {
  def apiCall(principal: Principal): String = {
     app.getFacebookApi().getMePhotos(getPrincipal());
  }
}
