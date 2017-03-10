package io.skysail.app.instagram.resources

import io.skysail.server.restlet.resources.EntityServerResource
import com.fasterxml.jackson.databind.ObjectMapper
import io.skysail.app.instagram.domain.Media
import io.skysail.app.instagram.domain.InstagramMedia
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.skysail.app.instagram.InstagramApplication
import io.skysail.app.instagram.MeRecentResource
import io.skysail.app.instagram.domain.Media
import io.skysail.core.app.ApplicationProvider
import io.skysail.server.menus.MenuItemProvider
import org.osgi.service.component.annotations.Component

object MediaResource { val mapper = new ObjectMapper }

class MediaResource extends EntityServerResource[InstagramMedia] {
  def getEntity(): InstagramMedia = {
    val app = getApplication().asInstanceOf[InstagramApplication]
    val json = app.getInstagramApi().getMe(getPrincipal())
    println(json)
    val result = MeRecentResource.mapper.readValue(json, classOf[Media])
    println(result)
    new InstagramMedia(result)
    //MeRecentResource.mapper.readValue(json, classOf[UserWrapper])
  }
}
