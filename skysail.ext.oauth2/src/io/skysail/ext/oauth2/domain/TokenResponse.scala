package io.skysail.ext.oauth2.domain

import org.json.JSONObject

case class TokenResponse(
  accessToken: String,
  tokenType: String,
  expirePeriod: Int,
  scope: String) extends Token

object TokenResponse {
  def apply(json: JSONObject):TokenResponse = {
     new TokenResponse(
    json.getString("access_token"),
    json.getString("token_type"),
    json.getInt("expires_in"),
     "")
    //        ""
    //        );
    //this.scope = "";//Scopes.parseScope(json.getString("scope"));
  }
}