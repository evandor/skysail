package io.skysail.api.um;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authenticator;

/**
 * Authenticator which always returns "true".
 *
 */
public class AlwaysAuthenticatedAuthenticator extends Authenticator  {

	public AlwaysAuthenticatedAuthenticator(Context context) {
		super(context);
	}

	@Override
	protected boolean authenticate(Request request, Response response) {
		return true;
	}

}
