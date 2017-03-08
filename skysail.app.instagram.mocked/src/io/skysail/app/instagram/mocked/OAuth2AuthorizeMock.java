package io.skysail.app.instagram.mocked;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class OAuth2AuthorizeMock extends ServerResource {

	@Get
	public Representation get() {
		getResponse().redirectSeeOther(getQueryValue("redirect_uri") + "?code=xxx");
		return null;
	}
}
