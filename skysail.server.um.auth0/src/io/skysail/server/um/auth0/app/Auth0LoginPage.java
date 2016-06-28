package io.skysail.server.um.auth0.app;

import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.um.domain.Credentials;

public class Auth0LoginPage extends PostEntityServerResource<Credentials> {

	@Override
	public Credentials createEntityTemplate() {
		return new Credentials();
	}

}
