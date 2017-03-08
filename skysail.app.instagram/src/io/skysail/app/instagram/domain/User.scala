package io.skysail.app.instagram.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.skysail.domain.Entity

@JsonIgnoreProperties(ignoreUnknown = true)
class User extends Entity {
  var id: String = ""
  var username: String = ""

  def getId(): String = id
  def setId(id: String): Unit = this.id = id;
  
 // def getMeta(): String = meta
 // def setMeta(meta: String): Unit = this.meta = meta;
  
}