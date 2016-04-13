package io.skysail.api.um;

import java.security.Principal;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.security.Authenticator;

import aQute.bnd.annotation.ProviderType;
import org.restlet.security.User;

@ProviderType
public interface AuthenticationService {

	Authenticator getAuthenticator(String pathTemplate, Context context);

	boolean isAuthenticated(Request request);

	Principal getPrincipal(Request request);

	void updatePassword(User user, String newPassword);

	// Needed?
	void clearCache(String username);

}
