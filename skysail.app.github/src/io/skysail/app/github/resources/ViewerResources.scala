package io.skysail.app.github.resources

import io.skysail.server.restlet.resources.EntityServerResource
import io.skysail.domain.GenericIdentifiable
import java.util.Arrays

class ViewerUserResource extends EntityServerResource[GenericIdentifiable] {
  def getEntity(): GenericIdentifiable = new GenericIdentifiable("")
  override def getPolymerUiExtensions():java.util.List[String] = Arrays.asList("sky-left-nav", "sky-content-user")  
}

class ViewerRepositoriesResource extends EntityServerResource[GenericIdentifiable] {
  def getEntity(): GenericIdentifiable = new GenericIdentifiable("")
  override def getPolymerUiExtensions():java.util.List[String] = Arrays.asList("sky-left-nav", "sky-content-repositories")  
}