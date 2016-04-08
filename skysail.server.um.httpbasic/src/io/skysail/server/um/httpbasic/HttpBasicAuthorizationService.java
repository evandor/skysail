package io.skysail.server.um.httpbasic;

import java.util.Collections;
import java.util.Set;

import org.restlet.data.ClientInfo;
import org.restlet.security.Enroler;
import org.restlet.security.Role;

import io.skysail.api.um.AuthorizationService;

public class HttpBasicAuthorizationService implements AuthorizationService, Enroler {

	public HttpBasicAuthorizationService(HttpBasicUserManagementProvider keycloakUserManagementProvider) {
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
