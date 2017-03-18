package io.skysail.app.github.resources

import io.skysail.server.restlet.resources.EntityServerResource
import io.skysail.domain.GenericIdentifiable
import java.security.Principal
import java.util.Arrays
import io.skysail.app.github.GithubApplication

abstract class GithubResource extends EntityServerResource[GenericIdentifiable] {
  var app = getApplication().asInstanceOf[GithubApplication];
  def apiCall(principal: Principal): String
  def getEntity(): GenericIdentifiable = {
    new GenericIdentifiable(apiCall(getPrincipal()))
  }
}

class RepositoriesResource extends GithubResource {
  def apiCall(principal: Principal): String = app.getApi().getRepositories(getPrincipal())
  //override def getPolymerUiExtensions(): java.util.List[String] = Arrays.asList("sky-content-friends")
}

class UserResource extends GithubResource {
  def apiCall(principal: Principal): String = app.getApi().getUser(getPrincipal())
  override def getPolymerUiExtensions(): java.util.List[String] = Arrays.asList("sky-content-user")
}
