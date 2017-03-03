package io.skysail.ext.oauth2

import io.skysail.ext.oauth2.resources.AccessTokenClientResource
import org.osgi.service.component.annotations.Component
import io.skysail.ext.oauth2.domain.Token
import io.skysail.ext.oauth2.domain.OAuth2Parameters
import org.restlet.data.Reference

trait AccessTokenClientResourceProvider {
  def get(): AccessTokenClientResource
}

@Component
class DefaultAccessTokenClientResourceProvider extends AccessTokenClientResourceProvider {
  def get(): AccessTokenClientResource = {
    ???
  }
}