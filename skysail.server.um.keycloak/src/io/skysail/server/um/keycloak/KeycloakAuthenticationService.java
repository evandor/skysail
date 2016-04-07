package io.skysail.server.um.keycloak;

import org.restlet.Context;
import org.restlet.security.Authenticator;

import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.User;

public class KeycloakAuthenticationService implements AuthenticationService {

	public KeycloakAuthenticationService(KeycloakUserManagementProvider keycloakUserManagementProvider) {
	}

	@Override
	public Authenticator getAuthenticator(Context context) {
		return null;
	}

	@Override
	public void updatePassword(User user, String newPassword) {
		
	}

	@Override
	public void clearCache(String username) {
		
	}

}
