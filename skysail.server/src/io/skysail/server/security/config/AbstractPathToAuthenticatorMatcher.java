package io.skysail.server.security.config;

import java.lang.reflect.Constructor;

import org.restlet.Context;
import org.restlet.security.Authenticator;

import io.skysail.api.um.AuthenticationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractPathToAuthenticatorMatcher implements PathToAuthenticatorMatcher {

	protected Class<? extends Authenticator> authenticatorClass;

	@Override
	public void permitAll() {
		authenticatorClass = AlwaysAuthenticatedAuthenticator.class;
	}

	@Override
	public void authenticated() {
		authenticatorClass = AuthenticatedAuthenticator.class;
	}

	@Override
	public void denyAll() {
		authenticatorClass = NeverAuthenticatedAuthenticator.class;
	}
	
	@Override
	public Authenticator getAuthenticator(Context context, AuthenticationService authenticationService) {
		if (AuthenticatedAuthenticator.class == authenticatorClass) {
			return authenticationService.getAuthenticator(context);
		}
		try {
			Constructor<? extends Authenticator> constructor = authenticatorClass.getConstructor(Context.class);
			return constructor.newInstance(context);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}



}
