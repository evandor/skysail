package io.skysail.app.instagram

import io.skysail.server.restlet.resources.EntityServerResource
import io.skysail.domain.GenericIdentifiable
import com.fasterxml.jackson.databind.ObjectMapper
import io.skysail.app.instagram.domain.Recent
import io.skysail.app.instagram.domain.User

class MeResource extends EntityServerResource[GenericIdentifiable] {
  def getEntity(): GenericIdentifiable = {
    val app = getApplication().asInstanceOf[InstagramApplication];
    new GenericIdentifiable(app.getInstagramApi().getMe(getPrincipal()));
  }
}
object MeRecentResource { val mapper = new ObjectMapper }

class MeRecentResource extends EntityServerResource[Recent] {
  def getEntity(): Recent = {
    val app = getApplication().asInstanceOf[InstagramApplication]
    val json = app.getInstagramApi().getMeRecent(getPrincipal())
    MeRecentResource.mapper.readValue(json, classOf[Recent])
  }
}

object SelfResource { val mapper = new ObjectMapper }

class SelfResource extends EntityServerResource[User] {
  def getEntity(): User = {
    val app = getApplication().asInstanceOf[InstagramApplication]
    val json = app.getInstagramApi().getMe(getPrincipal())
    MeRecentResource.mapper.readValue(json, classOf[User])
  }
}
