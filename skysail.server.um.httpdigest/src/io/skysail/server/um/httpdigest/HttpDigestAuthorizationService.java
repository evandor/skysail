package io.skysail.server.um.httpdigest;

import java.util.Collections;
import java.util.Set;

import org.restlet.data.ClientInfo;
import org.restlet.security.Enroler;
import org.restlet.security.Role;

import io.skysail.api.um.AuthorizationService;

public class HttpDigestAuthorizationService implements AuthorizationService, Enroler {

	@Override
	public Set<Role> getRolesFor(String username) {
		return Collections.emptySet();
	}

	@Override
	public void enrole(ClientInfo clientInfo) {
	}

}
