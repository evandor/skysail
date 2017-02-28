package io.skysail.ext.oauth2

class OAuth2ServerParameters(authUri: String, tokenUri: String) {
  def getAuthUri(): String = authUri
  def getTokenUri(): String = tokenUri
}