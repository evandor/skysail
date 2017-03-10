package io.skysail.app.instagram.domain

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.skysail.domain.Entity
import io.skysail.domain.html.Field

@JsonIgnoreProperties(ignoreUnknown = true)
case class Media(
  @JsonProperty("data") val data: MediaData,
  @JsonProperty("meta") val meta: MediaMeta
)

@JsonIgnoreProperties(ignoreUnknown = true)
case class MediaMeta(
  @JsonProperty("code") val code: Int
)

@JsonIgnoreProperties(ignoreUnknown = true)
case class MediaData(
  @JsonProperty("caption") val caption: MediaDataCaption
)

@JsonIgnoreProperties(ignoreUnknown = true)
case class MediaDataCaption(
  @JsonProperty("created_time") val createdTime: String
)
