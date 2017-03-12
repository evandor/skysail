package io.skysail.ext.oauth2

import io.skysail.ext.oauth2.config.OAuth2ConfigDescriptor

class OAuth2ClientParameters(val config: OAuth2ConfigDescriptor) {
  
  def clientId() = config.clientId()
  def clientSecret() = config.clientSecret()
  def scope() = config.scope()
  def redirectUri() = config.redirectUri()

}
