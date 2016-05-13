package io.skysail.api.um;

import java.security.Principal;

import org.osgi.annotation.versioning.ProviderType;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.security.Authenticator;

@ProviderType
public interface AuthenticationService {

	Authenticator getApplicationAuthenticator(Context context);

	Authenticator getResourceAuthenticator(Context context);

	boolean isAuthenticated(Request request);

	Principal getPrincipal(Request request);

	String getLoginPath();

	String getLogoutPath();
}
