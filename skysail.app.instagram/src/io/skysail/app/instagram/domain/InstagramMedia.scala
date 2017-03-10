package io.skysail.app.instagram.domain

import io.skysail.domain.html.Field
import io.skysail.domain.Entity
import io.skysail.domain.html.InputType

class InstagramMedia(media: Media) extends Entity {
  def getId(): String = "";//user.data.id

//  @Field 
//  var username = "";
//  def getUsername() = user.data.username
//
//  @Field(inputType = InputType.IMAGE)
//  var picture = "";
//  def getPicture() = user.data.picture
//
//  @Field 
//  var following = 0;
//  def getFollowing() = user.data.counts.follows
//
//  @Field 
//  var followed = 0;
//  def getFollowed() = user.data.counts.followedBy

}