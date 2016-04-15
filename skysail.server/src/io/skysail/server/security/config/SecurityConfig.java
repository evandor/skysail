package io.skysail.server.security.config;

import java.util.ArrayList;
import java.util.List;

import org.restlet.Context;
import org.restlet.security.Authenticator;

import io.skysail.api.um.AuthenticationService;

public class SecurityConfig {

	private final List<PathToAuthenticatorMatcher> matchers = new ArrayList<>();

	private AuthenticationService authenticationService;
	
	public SecurityConfig(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public Authenticator authenticatorFor(Context context, String path) {
		return matchers.stream()
			.filter(pathExpression -> pathExpression.match(path))
			.findFirst().map(pE -> pE.getAuthenticator(context, authenticationService))
			.orElse(new NeverAuthenticatedAuthenticator(context));		
	}

	public void match(PathToAuthenticatorMatcher pathToAuthenticatorMatcher) {
		matchers.add(pathToAuthenticatorMatcher);
	}

}
