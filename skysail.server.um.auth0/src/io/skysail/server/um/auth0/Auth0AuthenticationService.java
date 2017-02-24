package io.skysail.server.um.auth0;

import java.security.Principal;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.security.Authenticator;

import io.skysail.api.links.Link;
import io.skysail.api.um.AuthenticationMode;
import io.skysail.api.um.AuthenticationService;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.um.auth0.app.Auth0LoginPage;
import io.skysail.server.um.auth0.app.Auth0UmApplication;
import io.skysail.server.utils.LinkUtils;

public class Auth0AuthenticationService implements AuthenticationService {

	private Auth0UserManagementProvider userManagementProvider;

	public Auth0AuthenticationService(Auth0UserManagementProvider userManagementProvider) {
		this.userManagementProvider = userManagementProvider;
	}

	@Override
	public Authenticator getApplicationAuthenticator(Context context, AuthenticationMode authMode) {
		return new Authenticator(context) {
			@Override
			protected boolean authenticate(Request request, Response response) {
				return true;
			}
		};
	}

	@Override
	public Authenticator getResourceAuthenticator(Context context, AuthenticationMode authMode) {
		return null;
	}

	@Override
	public boolean isAuthenticated(Request request) {
		return false;
	}

	@Override
	public Principal getPrincipal(Request request) {
		return null;
	}

	@Override
	public String getLoginPath() {
		try {
			Link httpBasicLoginPageLink = LinkUtils.fromResource(
					userManagementProvider.getSkysailApplication().getApplication(), Auth0LoginPage.class);
			return httpBasicLoginPageLink.getUri();
		} catch (Exception e) { // NOSONAR
			return "/" + Auth0UmApplication.class.getSimpleName() + "/v1" + SkysailRootApplication.LOGIN_PATH;
		}
	}

	@Override
	public String getLogoutPath() {
		return null;
	}

}
