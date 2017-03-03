package io.skysail.ext.oauth2.resources

import org.restlet.resource.ClientResource
import org.restlet.data.Reference
import org.restlet.Response
import org.restlet.representation.Representation
import org.restlet.resource.ResourceException
import org.restlet.data.MediaType

class AccessTokenClientResource(tokenUri: Reference) extends ClientResource(tokenUri) {

  override def handleInbound(response: Response): Representation = {
    try {
      return super.handleInbound(response);
    } catch {
      case e: ResourceException => handleFooException(e)
      //case _: Throwable => println("Got some other kind of exception")
    }
  }
  
  def handleFooException(e: ResourceException): Representation = {
    if (getResponse().isEntityAvailable() && MediaType.APPLICATION_JSON.equals(getResponseEntity().getMediaType())) {
      return getResponseEntity();
    }
    throw e
  }
}