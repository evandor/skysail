package io.skysail.ext.oauth2.domain

import org.restlet.data.Form
import org.restlet.data.Reference
import org.restlet.representation.Representation

class OAuth2Parameters {

  val form: Form = new Form();

  def code(code: String): OAuth2Parameters = {
    return add("code", code)
  }

  def grantType(grantType: GrantType): OAuth2Parameters = {
    return add("grant_type", grantType.name)
  }

  def responseType(responseType: ResponseType): OAuth2Parameters = {
    return add("response_type", responseType.name)
  }

  def redirectUri(redirectUri: String): OAuth2Parameters = {
    return add("redirect_uri", redirectUri)
  }

  def add(name: String, value: String): OAuth2Parameters = {
    form.add(name, value);
    return this;
  }

  def toReference(uri: String): Reference = {
    try {
      val reference = new Reference(uri);
      reference.setQuery(form.encode());

      return reference;
      //} catch {
      //case _: throw new RuntimeException();//ResourceException(Status.SERVER_ERROR_INTERNAL, "Issue when encoding the OAuth parameters.", _);
    }
  }

  def toRepresentation(): Representation = {
    form.getWebRepresentation()
  }
}