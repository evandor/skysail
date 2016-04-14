package io.skysail.server.security.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.restlet.Context;
import org.restlet.security.Authenticator;

public class SecurityConfig {

	private final LinkedHashMap<String, SecurityConfigMode> authenticators = new LinkedHashMap<>();
	
	private final List<PathExpression> pathExpressions = new ArrayList<>();
	
	public Authenticator authenticatorFor(Context context, String path) {
		return pathExpressions.stream()
			.filter(pathExpression -> pathExpression.match(path))
			.findFirst().map(pE -> pE.getAuthenticator(context))
			.orElse(new NeverAuthenticatedAuthenticator(context));
		
		
//		return authenticators.keySet().stream()
//			.filter(authPath -> authPath.equals(path))
//			.findFirst()
//			.map(authPath -> authenticators.get(path).getAuthenticator(context))
//			.orElse(new UnauthenticatedAuthenticator(context));
	}

	public void match(String path, SecurityConfigMode mode) {
		authenticators.put(path, mode);
	}

	public void match(PathExpression pathExpression) {
		pathExpressions.add(pathExpression);
	}

}
