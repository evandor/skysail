package io.skysail.app.instagram.mocked;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class OAuth2TokenMock extends ServerResource {

	@Post
	public Representation post() {
		return new JsonRepresentation("{\"access_token\":\"thetoken\"}");
	}
}
