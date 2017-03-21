package io.skysail.app.notes.domain

import io.skysail.domain.Entity
import io.skysail.domain.html.Field
import io.skysail.domain.html.InputType
import javax.validation.constraints.Size

class Note() extends Entity {
  var id:String = null

  @Field(inputType = InputType.TEXTAREA)
  @Size(min=1)
  var content: String = ""
  
 // var owner: String = ""
  
  def getId(): String = id
  //def getOwner() = owner
  def setContent(c: String) = content = c
  def getContent() = content
}