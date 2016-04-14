package io.skysail.server.security.config;

import java.lang.reflect.Constructor;

import org.restlet.Context;
import org.restlet.security.Authenticator;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StartsWithPathExpression implements PathExpression {

	private String startsWith;

	private Class<? extends Authenticator> authenticatorClass;

	public StartsWithPathExpression(String startsWith) {
		this.startsWith = startsWith;
	}

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
	public boolean match(@NonNull String path) {
		return path.startsWith(startsWith);
	}

	@Override
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
