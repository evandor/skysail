package io.skysail.ext.oauth2.domain

import org.json.JSONObject

case class TokenResponse(accessToken: String) extends Token

object TokenResponse {
  def apply(json: JSONObject):TokenResponse = {
     new TokenResponse(json.getString("access_token"))
  }
}