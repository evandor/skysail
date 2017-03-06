package io.skysail.ext.oauth2.components

import io.skysail.ext.oauth2.resources.AccessTokenClientResource
import org.restlet.data.Reference
import org.junit.Before

import org.junit.Test

import org.assertj.core.api.Assertions._;
import io.skysail.ext.oauth2.resources.DefaultAccessTokenClientResource
import org.mockito.Mockito

class AccessTokenClientResourceCollectorTest {

  var con: AccessTokenClientResourceCollector = null

  @Before
  def setup() {
    con = new AccessTokenClientResourceCollector()
  }

  @Test
  def added_resource_is_available() {
    con.addAccessTokenResource(Mockito.mock(classOf[AccessTokenClientResource]))
    assertThat(AccessTokenClientResourceCollector.elements.size).isEqualTo(1)
  }

  @Test
  def removed_resource_is_not_available() {
    val atcr1 = new DefaultAccessTokenClientResource(new Reference("uri"))
    val atcr2 = new DefaultAccessTokenClientResource(new Reference("uri2"))
    
    con.addAccessTokenResource(atcr1)
    con.addAccessTokenResource(atcr2)
    
    assertThat(AccessTokenClientResourceCollector.elements.size).isGreaterThanOrEqualTo(2)
    
    con.removeAccessTokenResource(atcr1)
    
    assertThat(AccessTokenClientResourceCollector.elements.filter { x => x == atcr1 }.size).isEqualTo(0)
  }

}