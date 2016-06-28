package io.skysail.server.um.auth0;

import java.util.Set;

import org.restlet.security.Enroler;
import org.restlet.security.Role;

import io.skysail.api.um.AuthorizationService;

public class Auth0AuthorizationService implements AuthorizationService {

	public Auth0AuthorizationService(Auth0UserManagementProvider auth0UserManagementProvider) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Set<Role> getRolesFor(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Enroler getEnroler() {
		// TODO Auto-generated method stub
		return null;
	}

}
