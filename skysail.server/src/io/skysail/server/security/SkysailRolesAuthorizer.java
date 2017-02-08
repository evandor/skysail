package io.skysail.server.security;

import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authorizer;

import io.skysail.core.app.SkysailApplication;

public class SkysailRolesAuthorizer extends Authorizer {

    private List<String> roleNames;
	private SkysailApplication skysailApplication;

    public SkysailRolesAuthorizer(SkysailApplication skysailApplication, List<String> roleNames) {
        this.skysailApplication = skysailApplication;
		this.roleNames = roleNames;
    }

    @Override
    protected boolean authorize(Request request, Response response) {
        if (!skysailApplication.isAuthenticated(request)) {
            return false;
        }
        throw new NotImplementedException();
    }

    @Override
    public String toString() {
        return "SkysailRolesAuthorizer (" + roleNames + ")";
    }
}
