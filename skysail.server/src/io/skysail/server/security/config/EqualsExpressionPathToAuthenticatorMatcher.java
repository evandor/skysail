package io.skysail.server.security.config;

import io.skysail.core.app.ApiVersion;
import lombok.NonNull;

public class EqualsExpressionPathToAuthenticatorMatcher extends AbstractPathToAuthenticatorMatcher {

    private String matcherString;

    public EqualsExpressionPathToAuthenticatorMatcher(SecurityConfigBuilder securityConfigBuilder,
            @NonNull String matcherString) {
        super(securityConfigBuilder);
        this.securityConfigBuilder = securityConfigBuilder;
        ApiVersion apiVersion = securityConfigBuilder.getApiVersion();
        this.matcherString = apiVersion == null ? matcherString : apiVersion.getVersionPath() + matcherString;
    }

    @Override
    public boolean match(@NonNull String path) {
        return path.equals(matcherString);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName()).append(": ");
        sb.append(matcherString).append(" -> ").append(authenticatorClass);
        return sb.toString();
    }
}
