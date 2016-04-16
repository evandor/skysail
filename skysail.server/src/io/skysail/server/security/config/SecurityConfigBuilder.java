package io.skysail.server.security.config;

import java.util.List;

import io.skysail.api.um.AuthenticationService;
import io.skysail.server.app.ApiVersion;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class SecurityConfigBuilder {

	@Getter
	private PathToAuthenticatorMatcherRegistry pathToAuthenticatorMatcherRegistry = new PathToAuthenticatorMatcherRegistry(this);

	@Setter
	private AuthenticationService authenticationService;

	@Getter
	private ApiVersion apiVersion;

	public SecurityConfigBuilder(ApiVersion apiVersion) {
		this.apiVersion = apiVersion;
	}

	public PathToAuthenticatorMatcherRegistry authorizeRequests() {
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
