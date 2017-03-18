package $basePackageName$;

import io.skysail.server.restlet.resources.EntityServerResource
import io.skysail.domain.GenericIdentifiable
import java.security.Principal
import java.util.Arrays

abstract class TemplateResource extends EntityServerResource[GenericIdentifiable] {
  var app = getApplication().asInstanceOf[TemplateApplication];
  def apiCall(principal: Principal): String
  def getEntity(): GenericIdentifiable = {
    new GenericIdentifiable(apiCall(getPrincipal()))
  }
}

class RepositoriesResource extends TemplateResource {
  def apiCall(principal: Principal): String = app.getApi().getRepositories(getPrincipal())
  //override def getPolymerUiExtensions(): java.util.List[String] = Arrays.asList("sky-content-friends")
}

class UserResource extends TemplateResource {
  def apiCall(principal: Principal): String = app.getApi().getUser(getPrincipal())
  //override def getPolymerUiExtensions(): java.util.List[String] = Arrays.asList("sky-content-friends")
}
