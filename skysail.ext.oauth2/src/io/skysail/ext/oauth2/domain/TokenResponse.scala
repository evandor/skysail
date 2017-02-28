package io.skysail.ext.oauth2.domain

import org.json.JSONObject

case class TokenResponse(
  accessToken: String,
  tokenType: String,
  expirePeriod: Int,
  refreshToken: String,
  scope: String) extends Token

object TokenResponse {
  def apply(json: JSONObject) = {

    def accessToken = json.getString("access_token")
    def tokenType = json.getString("token_type");
    def expirePeriod = json.getInt("expires_in");
    def refreshToken = json.getString("refresh_token");
    //        ""
    //        );
    //this.scope = "";//Scopes.parseScope(json.getString("scope"));
  }
}