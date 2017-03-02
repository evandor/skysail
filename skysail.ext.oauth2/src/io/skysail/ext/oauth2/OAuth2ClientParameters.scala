package io.skysail.ext.oauth2

class OAuth2ClientParameters(
    val clientId: String, 
    val clientSecret: String, 
    val scope: String, 
    val redirectUri: String) {
}