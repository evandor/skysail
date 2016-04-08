package io.skysail.server.um.keycloak;

import java.util.Collections;
import java.util.Set;

import org.restlet.data.ClientInfo;
import org.restlet.security.Enroler;
import org.restlet.security.Role;

import io.skysail.api.um.AuthorizationService;

public class KeycloakAuthorizationService implements AuthorizationService, Enroler {

	public KeycloakAuthorizationService(KeycloakUserManagementProvider keycloakUserManagementProvider) {
	}

	@Override
	public Set<Role> getRolesFor(String username) {
		return Collections.emptySet();
	}

	@Override
	public void enrole(ClientInfo clientInfo) {
		System.out.println(clientInfo);
	}

}
