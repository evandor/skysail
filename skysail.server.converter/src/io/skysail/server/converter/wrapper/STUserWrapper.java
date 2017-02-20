package io.skysail.server.converter.wrapper;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.restlet.data.ClientInfo;

import io.skysail.api.um.UserManagementProvider;
import io.skysail.core.resources.SkysailServerResource;

public class STUserWrapper {

    private static final String DEMO = "demo";

	private static final String ANONYMOUS = "anonymous";

    private UserManagementProvider userManagementProvider;
    private String peerName;
	private SkysailServerResource<?> resource;

    public STUserWrapper(UserManagementProvider ump, SkysailServerResource<?> resource, String peerName) {
        this.userManagementProvider = ump;
		this.resource = resource;
        this.peerName = peerName;
    }

    public Principal getPrincipal() {
        Principal principal = userManagementProvider.getAuthenticationService().getPrincipal(resource.getRequest());
        return principal.getName().equals(ANONYMOUS) ? null : principal;

    }

    public String getUsername() {
    	return getPrincipal() == null ? ANONYMOUS : getPrincipal().getName();
    }

    public boolean isDeveloper() {
        ClientInfo clientInfo = this.resource.getRequest().getClientInfo();
        //this.userManagementProvider.getAuthorizationService().getEnroler().enrole(clientInfo);
        return true;//subject.hasRole("developer");
    }

    public boolean isAdmin() {
        return true;//subject.hasRole("admin");
    }

    public boolean isDemoUser() {
        return getUsername().equals(DEMO);
    }

    public String getBackend() {
        if (peerName == null || peerName.trim().length() == 0) {
            return "";
        }
        return "["+peerName+"] ";
    }

    public List<String> getPeers() {
    	return Collections.emptyList();
    }
}
