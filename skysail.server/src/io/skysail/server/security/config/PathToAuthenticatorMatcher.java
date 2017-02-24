package io.skysail.server.security.config;

import org.restlet.Context;
import org.restlet.security.Authenticator;

import io.skysail.api.um.AuthenticationService;

public interface PathToAuthenticatorMatcher {

	PathToAuthenticatorMatcherRegistry permitAll();
	PathToAuthenticatorMatcherRegistry denyAll();
	PathToAuthenticatorMatcherRegistry authenticated();
	PathToAuthenticatorMatcherRegistry anonymous();

	boolean match(String path);

	Authenticator getAuthenticator(Context context, AuthenticationService authenticationService);


}
