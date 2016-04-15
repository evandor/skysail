package io.skysail.server.security.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.restlet.Context;
import org.restlet.security.Authenticator;

import io.skysail.api.um.AuthenticationService;

public class SecurityConfig {

	private final LinkedHashMap<String, SecurityConfigMode> authenticators = new LinkedHashMap<>();
	
	private final List<PathToAuthenticatorMatcher> pathExpressions = new ArrayList<>();

	private AuthenticationService authenticationService;
	
	public SecurityConfig(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public Authenticator authenticatorFor(Context context, String path) {
		return pathExpressions.stream()
			.filter(pathExpression -> pathExpression.match(path))
			.findFirst().map(pE -> pE.getAuthenticator(context, authenticationService))
			.orElse(new NeverAuthenticatedAuthenticator(context));		
	}

	public void match(String path, SecurityConfigMode mode) {
		authenticators.put(path, mode);
	}

	public void match(PathToAuthenticatorMatcher pathExpression) {
		pathExpressions.add(pathExpression);
	}

}
