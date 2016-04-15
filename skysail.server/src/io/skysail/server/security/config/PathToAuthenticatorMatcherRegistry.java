package io.skysail.server.security.config;

import java.util.ArrayList;
import java.util.List;

import io.skysail.server.app.ApiVersion;
import lombok.Getter;

public class PathToAuthenticatorMatcherRegistry {

	@Getter
	private List<PathToAuthenticatorMatcher> matchers = new ArrayList<>();

	private ApiVersion apiVersion;

	public PathToAuthenticatorMatcherRegistry(ApiVersion apiVersion) {
		this.apiVersion = apiVersion;
	}

	public PathToAuthenticatorMatcher startsWithMatcher(String startsWith) {
		AbstractPathToAuthenticatorMatcher matcher = new StartsWithExpressionPathToAuthenticatorMatcher(apiVersion, startsWith);
		matchers.add(matcher);
		return matcher;
	}

}
