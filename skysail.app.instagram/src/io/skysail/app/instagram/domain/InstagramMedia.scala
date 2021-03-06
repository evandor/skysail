package io.skysail.app.instagram.domain

import io.skysail.domain.html.Field
import io.skysail.domain.Entity
import io.skysail.domain.html.InputType
import scala.collection.JavaConverters._

class InstagramMedia(media: Media) extends Entity {
  def getId(): String = "";//user.data.id

 /* @Field 
  var code = 0;
  def getCode() = media.meta.code
*/
  @Field 
  var mediaDataList:List[InstagramMediaData] = null
  def getMediaDataList() = 
      media.data.iterator().asScala.map(
          md => new InstagramMediaData(md)
      ).toList
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