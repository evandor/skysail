package io.skysail.server.security.config;

import org.restlet.Context;
import org.restlet.security.Authenticator;

public interface PathExpression {

	void permitAll();
	void denyAll();
	void authenticated();

	boolean match(String path);

	Authenticator getAuthenticator(Context context);


}
