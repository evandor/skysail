package io.skysail.server.security.config;

import java.lang.reflect.Constructor;

import org.restlet.Context;
import org.restlet.security.Authenticator;

import io.skysail.api.um.AuthenticationMode;
import io.skysail.api.um.AuthenticationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractPathToAuthenticatorMatcher implements PathToAuthenticatorMatcher {

	private AuthenticationMode authMode;
	
    protected SecurityConfigBuilder securityConfigBuilder;

    public AbstractPathToAuthenticatorMatcher(SecurityConfigBuilder securityConfigBuilder) {
        this.securityConfigBuilder = securityConfigBuilder;
    }

    @Override
    public PathToAuthenticatorMatcherRegistry permitAll() {
        authMode = AuthenticationMode.PERMIT_ALL;
        return securityConfigBuilder.getPathToAuthenticatorMatcherRegistry();
    }
    
    @Override
	public PathToAuthenticatorMatcherRegistry anonymous() {
        authMode = AuthenticationMode.ANONYMOUS;
        return securityConfigBuilder.getPathToAuthenticatorMatcherRegistry();
	}

    @Override
    public PathToAuthenticatorMatcherRegistry authenticated() {
        authMode = AuthenticationMode.AUTHENTICATED;
        return securityConfigBuilder.getPathToAuthenticatorMatcherRegistry();
    }

    @Override
    public PathToAuthenticatorMatcherRegistry denyAll() {
        authMode = AuthenticationMode.DENY_ALL;
        return securityConfigBuilder.getPathToAuthenticatorMatcherRegistry();
    }

    @Override
    public Authenticator getAuthenticator(Context context, AuthenticationService authenticationService) {
    	return authenticationService.getResourceAuthenticator(context, authMode);
    }

}
