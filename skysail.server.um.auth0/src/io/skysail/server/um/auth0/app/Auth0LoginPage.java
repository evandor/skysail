package io.skysail.server.um.auth0.app;

import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.um.auth0.NonceUtils;
import io.skysail.server.um.auth0.SessionUtils;

public class Auth0LoginPage extends PostEntityServerResource<Credentials> {

	@Override
	public Credentials createEntityTemplate() {
		NonceUtils.addNonceToStorage(getContext().getAttributes(), getQueryValue(NonceUtils.NONCE_KEY));
		Credentials credentials = new Credentials();
		credentials.setState(SessionUtils.getState(getContext()));
		return credentials;
	}

}
