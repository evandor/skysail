package io.skysail.app.instagram

import io.skysail.server.restlet.resources.EntityServerResource
import io.skysail.domain.GenericIdentifiable
import com.fasterxml.jackson.databind.ObjectMapper
import io.skysail.app.instagram.domain.User
import io.skysail.core.resources.SkysailServerResource
import org.restlet.resource.Get
import io.skysail.api.responses.EntityServerResponse
import org.restlet.representation.Variant
import io.skysail.app.instagram.domain.InstagramUser
import io.skysail.app.instagram.domain.InstagramMedia

class MeResource extends EntityServerResource[GenericIdentifiable] {
  def getEntity(): GenericIdentifiable = {
    val app = getApplication().asInstanceOf[InstagramApplication];
    new GenericIdentifiable(app.getInstagramApi().getMe(getPrincipal()));
  }
}
object MeRecentResource { val mapper = new ObjectMapper }

class MeRecentResource extends EntityServerResource[InstagramMedia] {
  def getEntity(): InstagramMedia = {
    val app = getApplication().asInstanceOf[InstagramApplication]
    val json = app.getInstagramApi().getMeRecentMedia(getPrincipal())
    MeRecentResource.mapper.readValue(json, classOf[InstagramMedia])
  }
}

object SelfResource { val mapper = new ObjectMapper }

class SelfResource extends EntityServerResource[InstagramUser] {
  def getEntity(): InstagramUser = {
    val app = getApplication().asInstanceOf[InstagramApplication]
    val json = app.getInstagramApi().getMe(getPrincipal())
    println(json)
    val result = MeRecentResource.mapper.readValue(json, classOf[User])
    println(result)
    new InstagramUser(result)
    //MeRecentResource.mapper.readValue(json, classOf[UserWrapper])
  }
}
