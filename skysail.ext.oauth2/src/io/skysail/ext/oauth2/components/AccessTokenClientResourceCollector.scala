package io.skysail.ext.oauth2.components

import org.osgi.service.component.annotations.Component
import io.skysail.ext.oauth2.resources.AccessTokenClientResource
import org.osgi.service.component.annotations.Reference
import org.osgi.service.component.annotations.ReferencePolicy
import org.osgi.service.component.annotations.ReferenceCardinality

@Component(service = Array(classOf[AccessTokenClientResourceCollector]))
class AccessTokenClientResourceCollector {

  var accessTokenClientResources = new java.util.ArrayList[AccessTokenClientResource]

  @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
  def addAccessTokenResource(accessTokenClientResource: AccessTokenClientResource) {
    accessTokenClientResources.add(accessTokenClientResource);
  }
  def removeAccessTokenResource(accessTokenClientResource: AccessTokenClientResource) {
    accessTokenClientResources.remove(accessTokenClientResource);
  }

}