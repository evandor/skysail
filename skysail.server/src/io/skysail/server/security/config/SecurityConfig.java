package io.skysail.server.security.config;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authenticator;

public class SecurityConfig {

	public static final String PERMIT_ALL = "permitAll";

	public Authenticator authenticatorFor(Context context, String path) {
		return new UnauthenticatedAuthenticator(context);
	}

	public void match(String string, String permitAll) {
		
	}

}
