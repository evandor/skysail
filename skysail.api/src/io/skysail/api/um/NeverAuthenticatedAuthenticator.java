package io.skysail.api.um;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.security.Authenticator;

/**
 * Authenticator which always returns "false".
 * 
 * Used by the security config as "catch-all".
 *
 */
public class NeverAuthenticatedAuthenticator extends Authenticator {

	public NeverAuthenticatedAuthenticator(Context context) {
		super(context);
	}

	@Override
	protected boolean authenticate(Request request, Response response) {
		response.setStatus(Status.CLIENT_ERROR_FORBIDDEN);
		return false;
	}

}
