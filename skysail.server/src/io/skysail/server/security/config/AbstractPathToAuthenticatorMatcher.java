package io.skysail.server.security.config;

import java.lang.reflect.Constructor;

import org.restlet.Context;
import org.restlet.security.Authenticator;

import io.skysail.api.um.AuthenticationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractPathToAuthenticatorMatcher implements PathToAuthenticatorMatcher {

    protected Class<? extends Authenticator> authenticatorClass = NeverAuthenticatedAuthenticator.class;

    protected SecurityConfigBuilder securityConfigBuilder;

    public AbstractPathToAuthenticatorMatcher(SecurityConfigBuilder securityConfigBuilder) {
        this.securityConfigBuilder = securityConfigBuilder;
    }

    @Override
    public PathToAuthenticatorMatcherRegistry permitAll() {
        authenticatorClass = AlwaysAuthenticatedAuthenticator.class;
        return securityConfigBuilder.getPathToAuthenticatorMatcherRegistry();
    }

    @Override
    public PathToAuthenticatorMatcherRegistry authenticated() {
        authenticatorClass = AuthenticatedAuthenticator.class;
        return securityConfigBuilder.getPathToAuthenticatorMatcherRegistry();
    }

    @Override
    public PathToAuthenticatorMatcherRegistry denyAll() {
        authenticatorClass = NeverAuthenticatedAuthenticator.class;
        return securityConfigBuilder.getPathToAuthenticatorMatcherRegistry();
    }

    @Override
    public Authenticator getAuthenticator(Context context, AuthenticationService authenticationService) {
        if (AuthenticatedAuthenticator.class == authenticatorClass) {
            return authenticationService.getResourceAuthenticator(context);
        }
        try {
            Constructor<? extends Authenticator> constructor = authenticatorClass.getConstructor(Context.class);
            return constructor.newInstance(context);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
