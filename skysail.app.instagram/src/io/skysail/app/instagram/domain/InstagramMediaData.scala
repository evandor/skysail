package io.skysail.app.instagram.domain

import io.skysail.domain.html.Field
import io.skysail.domain.Entity
import io.skysail.domain.html.InputType

class InstagramMediaData(mediaData: MediaData) extends Entity {
  def getId(): String = "";//user.data.id
  
  println(mediaData)

  @Field 
  var link = "xxx";
  def getLink() = mediaData.link

 /* @Field 
  var mediaList:java.util.List[InstagramMediaData] = null
  def getMediaList() = media.data.map(md => new InstagramMediaData(md)).toList()*/
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