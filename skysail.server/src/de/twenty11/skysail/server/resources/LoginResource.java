package de.twenty11.skysail.server.resources;

import de.twenty11.skysail.server.um.domain.Credentials;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.restlet.resources.RedirectResource;
import io.skysail.server.restlet.resources.SkysailServerResource;

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
