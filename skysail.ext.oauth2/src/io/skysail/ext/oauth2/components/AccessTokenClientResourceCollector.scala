package io.skysail.ext.oauth2.components

import org.osgi.service.component.annotations.Component
import io.skysail.ext.oauth2.resources.AccessTokenClientResource
import org.osgi.service.component.annotations.Reference
import org.osgi.service.component.annotations.ReferencePolicy
import org.osgi.service.component.annotations.ReferenceCardinality
import scala.collection.mutable.MutableList
import scala.collection.mutable.ListBuffer

object AccessTokenClientResourceCollector {
  val elements = ListBuffer[AccessTokenClientResource]()
}

@Component(immediate = true, service = Array(classOf[AccessTokenClientResourceCollector]))
class AccessTokenClientResourceCollector {

  @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
  def addAccessTokenResource(accessTokenClientResource: AccessTokenClientResource) {
    AccessTokenClientResourceCollector.elements+=accessTokenClientResource
  }
  
  def removeAccessTokenResource(accessTokenClientResource: AccessTokenClientResource) {
    AccessTokenClientResourceCollector.elements-=(accessTokenClientResource);
  }

}