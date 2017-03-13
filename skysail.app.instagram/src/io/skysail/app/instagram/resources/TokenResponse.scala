package io.skysail.app.instagram.resources

import org.json.JSONObject
import io.skysail.ext.oauth2.domain.Token

case class TokenResponse(accessToken: String) extends Token

object TokenResponse {
  def apply(json: JSONObject): TokenResponse = {
    new TokenResponse(
      json.getString("access_token"))
  }
}