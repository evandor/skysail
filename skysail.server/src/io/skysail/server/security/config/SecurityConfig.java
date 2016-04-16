package io.skysail.server.security.config;

import java.util.ArrayList;
import java.util.List;

import org.restlet.Context;
import org.restlet.security.Authenticator;

import io.skysail.api.um.AuthenticationService;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
public class SecurityConfig {

	private final List<PathToAuthenticatorMatcher> matchers = new ArrayList<>();

	private AuthenticationService authenticationService;
	
	public SecurityConfig(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public Authenticator authenticatorFor(Context context, String path) {
		Authenticator authenticator = matchers.stream()
			.filter(matcher -> matcher.match(path))
			.findFirst().map(matcher -> matcher.getAuthenticator(context, authenticationService))
			.orElse(new NeverAuthenticatedAuthenticator(context));		
		log.info("matched authenticators against path '{}', found '{}'",path, authenticator.getClass().getSimpleName());
		return authenticator;
	}

	public void match(PathToAuthenticatorMatcher pathToAuthenticatorMatcher) {
		matchers.add(pathToAuthenticatorMatcher);
	}

}
