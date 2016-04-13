package io.skysail.server.security.config;

import java.util.LinkedHashMap;

import org.restlet.Context;
import org.restlet.security.Authenticator;

public class SecurityConfig {

	private final LinkedHashMap<String, SecurityConfigMode> authenticators = new LinkedHashMap<>();
	
	public Authenticator authenticatorFor(Context context, String path) {
		return authenticators.keySet().stream()
			.filter(authPath -> authPath.equals(path))
			.findFirst()
			.map(authPath -> authenticators.get(path).getAuthenticator(context))
			.orElse(new UnauthenticatedAuthenticator(context));
	}

	public void match(String path, SecurityConfigMode mode) {
		authenticators.put(path, mode);
	}

}
