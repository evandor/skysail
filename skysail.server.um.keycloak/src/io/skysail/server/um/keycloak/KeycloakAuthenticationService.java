package io.skysail.server.um.keycloak;

import java.security.Principal;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.data.ChallengeScheme;
import org.restlet.security.Authenticator;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.User;

import io.skysail.api.um.AuthenticationService;

public class KeycloakAuthenticationService implements AuthenticationService {

	public KeycloakAuthenticationService(KeycloakUserManagementProvider keycloakUserManagementProvider) {
	}

	@Override
	public Authenticator getAuthenticator(Context context) {
		return new ChallengeAuthenticator(context, ChallengeScheme.HTTP_BASIC, "My Realm");
	}
	
    public Principal getPrincipal(Request request) {
    	return null;
    }

	
	@Override
	public boolean isAuthenticated(Request request) {
		return false;
	}

}
