package io.skysail.server.app.resources;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.restlet.resources.RedirectResource;
import io.skysail.server.um.domain.Credentials;

public class LoginResource extends RedirectResource<Credentials> {
	
    private SkysailRootApplication app;
    
	@Override
    protected void doInit() {
        app = (SkysailRootApplication) getApplication();
    }

    @Override
    public String redirectTo() {
        if (app.isAuthenticated(getRequest())) {
            return "/";
        }
        return app.getApplication().getAuthenticationService().getLoginPath();
    }

	@Override
	protected Class<? extends SkysailServerResource<?>> redirectToResource() {
		return null;
	}

}
