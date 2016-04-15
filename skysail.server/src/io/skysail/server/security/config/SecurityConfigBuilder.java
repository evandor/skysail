package io.skysail.server.security.config;

import java.util.List;

import io.skysail.api.um.AuthenticationService;
import io.skysail.server.app.ApiVersion;
import lombok.Setter;

public class SecurityConfigBuilder {

	private PathToAuthenticatorMatcherRegistry pathToAuthenticatorMatcherRegistry;

	@Setter
	private AuthenticationService authenticationService;

	private ApiVersion apiVersion;

	public SecurityConfigBuilder(ApiVersion apiVersion) {
		this.apiVersion = apiVersion;
	}

	public PathToAuthenticatorMatcherRegistry authorizeRequests() {
		pathToAuthenticatorMatcherRegistry = new PathToAuthenticatorMatcherRegistry(apiVersion);
		return pathToAuthenticatorMatcherRegistry;
	}

	public SecurityConfig build() {
		 SecurityConfig securityConfig = new SecurityConfig(authenticationService);
		 if (pathToAuthenticatorMatcherRegistry != null) {
			 List<PathToAuthenticatorMatcher> matchers = pathToAuthenticatorMatcherRegistry.getMatchers();
			 for (PathToAuthenticatorMatcher matcher : matchers) {
				securityConfig.match(matcher);
			}
		 }
		 return securityConfig;
	}


}
