package io.skysail.app.instagram.domain

import io.skysail.domain.Entity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class Recent extends Entity {
  var id: String = ""
  var meta: String = ""

  def getId(): String = id
  def setId(id: String): Unit = this.id = id;
  
 // def getMeta(): String = meta
 // def setMeta(meta: String): Unit = this.meta = meta;
  
}