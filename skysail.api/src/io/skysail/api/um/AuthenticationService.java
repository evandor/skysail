package io.skysail.api.um;

import java.security.Principal;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.security.Authenticator;

import aQute.bnd.annotation.ProviderType;

@ProviderType
public interface AuthenticationService {

	Authenticator getApplicationAuthenticator(Context context);

	Authenticator getResourceAuthenticator(Context context);

	boolean isAuthenticated(Request request);

	Principal getPrincipal(Request request);

}
