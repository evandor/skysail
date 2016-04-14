package io.skysail.server.security.config;

import java.lang.reflect.Constructor;

import org.restlet.Context;
import org.restlet.security.Authenticator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum SecurityConfigMode {

	AUTHENTICATED(AuthenticatedAuthenticator.class),
	ANONYMOUS(AlwaysAuthenticatedAuthenticator.class),
	PERMIT_ALL(AlwaysAuthenticatedAuthenticator.class),
	DENY_ALL(NeverAuthenticatedAuthenticator.class);

	private Class<? extends Authenticator> authenticatorClass;

	private SecurityConfigMode(Class<? extends Authenticator> authenticatorClass) {
		this.authenticatorClass = authenticatorClass;
	}

	public Authenticator getAuthenticator(Context context) {
		try {
			Constructor<? extends Authenticator> constructor = authenticatorClass.getConstructor(Context.class);
			return constructor.newInstance(context);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
	
}
