package io.skysail.server.um.auth0.app;

import org.restlet.data.Form;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.um.auth0.NonceUtils;
import io.skysail.server.um.auth0.SessionUtils;

public class Auth0LoginCallbackPage extends EntityServerResource<Credentials> {

	@Override
	public SkysailResponse<?> eraseEntity() {
		return null;
	}

	@Override
	public Credentials getEntity() {
		Form query = getQuery();
		System.out.println(query);
		return null;
	}


}
