package io.skysail.server.security.config;

import org.restlet.Context;
import org.restlet.security.Authenticator;

import io.skysail.api.um.AuthenticationService;

public interface PathToAuthenticatorMatcher {

	void permitAll();
	void denyAll();
	void authenticated();

	boolean match(String path);

	Authenticator getAuthenticator(Context context, AuthenticationService authenticationService);


}
