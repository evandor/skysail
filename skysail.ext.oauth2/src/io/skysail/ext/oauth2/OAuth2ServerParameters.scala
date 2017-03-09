package io.skysail.ext.oauth2

import io.skysail.ext.oauth2.config.OAuth2ConfigDescriptor

class OAuth2ServerParameters(val config: OAuth2ConfigDescriptor) {

  def apiBaseUrl() = config.apiBaseUrl()
  def authBaseUrl() = config.authBaseUrl()
  def authUri() = config.authUri()
  def tokenUri() = config.tokenUri()

}
