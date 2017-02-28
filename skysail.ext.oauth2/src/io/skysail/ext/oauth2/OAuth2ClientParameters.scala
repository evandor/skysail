package io.skysail.ext.oauth2

class OAuth2ClientParameters(clientId: String, clientSecret: String, redirectUri: String) {

  def getClientId(): String = clientId
  def getClientSecret(): String = clientSecret
  def getRedirectUri(): String = redirectUri
  
}