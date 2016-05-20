package io.skysail.server.um.httpbasic;

import java.util.Collections;
import java.util.Set;

import org.restlet.security.Enroler;
import org.restlet.security.Role;

import io.skysail.api.um.AuthorizationService;

public class HttpBasicAuthorizationService implements AuthorizationService {

	private HttpBasicUserManagementProvider provider;

    public HttpBasicAuthorizationService(HttpBasicUserManagementProvider provider) {
        this.provider = provider;
	}

	@Override
	public Set<Role> getRolesFor(String username) {
		return Collections.emptySet();
	}

    @Override
    public Enroler getEnroler() {
        return provider.getUserDetailsService().getEnroler();
    }

}
