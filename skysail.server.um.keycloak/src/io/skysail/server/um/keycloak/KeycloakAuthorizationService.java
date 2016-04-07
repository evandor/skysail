package io.skysail.server.um.keycloak;

import java.util.Set;

import org.restlet.security.Role;

import io.skysail.api.um.AuthorizationService;

public class KeycloakAuthorizationService implements AuthorizationService {

	public KeycloakAuthorizationService(KeycloakUserManagementProvider keycloakUserManagementProvider) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Set<Role> getRolesFor(String username) {
		return null;
	}

}
