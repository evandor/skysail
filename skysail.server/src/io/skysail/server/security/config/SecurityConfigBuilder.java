package io.skysail.server.security.config;

import java.util.List;

public class SecurityConfigBuilder {

	private PathExpressionRegistry pathExpressionRegistry;

	public PathExpressionRegistry authorizeRequests() {
		pathExpressionRegistry = new PathExpressionRegistry();
		return pathExpressionRegistry;
	}

	public SecurityConfig build() {
		 SecurityConfig securityConfig = new SecurityConfig();
		 if (pathExpressionRegistry != null) {
			 List<PathExpression> pathExpressions = pathExpressionRegistry.getEntries();
			 for (PathExpression pathExpression : pathExpressions) {
				securityConfig.match(pathExpression);
			}
		 }
		 return securityConfig;
	}

}
