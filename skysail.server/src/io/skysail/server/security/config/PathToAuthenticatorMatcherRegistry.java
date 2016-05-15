package io.skysail.server.security.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.ToString;

@ToString
public class PathToAuthenticatorMatcherRegistry {

	@Getter
	private List<PathToAuthenticatorMatcher> matchers = new ArrayList<>();

	private SecurityConfigBuilder securityConfigBuilder;

	public PathToAuthenticatorMatcherRegistry(SecurityConfigBuilder securityConfigBuilder) {
		this.securityConfigBuilder = securityConfigBuilder;
	}

	public PathToAuthenticatorMatcher startsWithMatcher(String startsWith) {
		AbstractPathToAuthenticatorMatcher matcher = new StartsWithExpressionPathToAuthenticatorMatcher(securityConfigBuilder, startsWith);
		matchers.add(matcher);
		return matcher;
	}

    public SecurityConfigBuilder and() {
        return securityConfigBuilder;
    }

}
