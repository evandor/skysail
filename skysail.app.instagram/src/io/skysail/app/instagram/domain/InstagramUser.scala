package io.skysail.app.instagram.domain

import io.skysail.domain.html.Field
import io.skysail.domain.Entity
import io.skysail.domain.html.InputType

class InstagramUser(user: User) extends Entity {
  def getId(): String = user.data.id

  @Field 
  var username = "";
  def getUsername():String = user.data.username

  @Field(inputType = InputType.IMAGE)
  var picture = "";
  def getPicture():String = user.data.picture

}