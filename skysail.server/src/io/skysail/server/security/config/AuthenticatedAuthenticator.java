package io.skysail.server.security.config;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authenticator;

public class AuthenticatedAuthenticator extends Authenticator {

	public AuthenticatedAuthenticator(Context context) {
		super(context);
	}

	@Override
	protected boolean authenticate(Request request, Response response) {
		return false;
	}

}
