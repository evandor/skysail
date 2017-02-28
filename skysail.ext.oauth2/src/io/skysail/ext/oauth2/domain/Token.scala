package io.skysail.ext.oauth2.domain

trait Token {
  
  def accessToken: String
  def expirePeriod: Int
  def refreshToken: String
  def scope: String;
  def tokenType: String

}