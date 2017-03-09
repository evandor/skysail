package io.skysail.app.instagram.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.skysail.domain.Entity
import io.skysail.domain.html.Field

@JsonIgnoreProperties(ignoreUnknown = true)
case class User(
  @JsonProperty("data") val data: Data,
  @JsonProperty("meta") val meta: Meta
) extends Entity {
  def getId():String = data.id
}

@JsonIgnoreProperties(ignoreUnknown = true)
case class Meta(
  @JsonProperty("code") val code: Int
)

@JsonIgnoreProperties(ignoreUnknown = true)
case class Data(
  @JsonProperty("id") val id: String,
  @JsonProperty("username") val username: String,
  @JsonProperty("full_name") val fullname: String,
  @JsonProperty("profile_picture") val picture: String,
  @JsonProperty("counts") val counts: Counts
)

@JsonIgnoreProperties(ignoreUnknown = true)
case class Counts(
  @JsonProperty("follows") val follows: Int,
  @JsonProperty("followed_by") val followedBy: Int,
  @JsonProperty("media") val media: Int
)
