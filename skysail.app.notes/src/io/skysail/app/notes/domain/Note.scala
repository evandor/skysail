package io.skysail.app.notes.domain

import io.skysail.domain.Entity

class Note() extends Entity {
  var id:String = ""
  var content: String = ""
  var owner: String = ""
  
  def getId(): String = id
  
  def getOwner() = owner
  
  def getContent() = content
}