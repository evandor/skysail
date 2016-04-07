package de.twenty11.skysail.server.resources;

import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.restlet.data.Form;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.um.domain.Credentials;
import io.skysail.api.responses.FormResponse;
import io.skysail.api.um.AuthenticationService;
import io.skysail.api.um.AuthenticatorProvider;
import io.skysail.api.um.UserManagementProvider;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class LoginResource extends PostEntityServerResource<Credentials> {
	
    private SkysailRootApplication app;
    
	@Override
    protected void doInit() throws ResourceException {
        app = (SkysailRootApplication) getApplication();
    }

    @Get("htmlform")
    public FormResponse<Credentials> createForm() {
        return new FormResponse<Credentials>(getResponse(), getEntity(), SkysailRootApplication.LOGIN_PATH);
    }

    @Override
    public Credentials getEntity() {
        return new Credentials();
    }

    @Override
    public Credentials getData(Form form) {
        if (form == null) {
            return new Credentials();
        }
        return new Credentials(form.getFirstValue("username"), form.getFirstValue("password"));
    }

    @Override
    public Credentials createEntityTemplate() {
        return new Credentials();
    }

    @Override
    public void addEntity(Credentials entity) {} // NOSONAR

    @Override
    public String redirectTo() {
        boolean authenticated = app.isAuthenticated();
        if (authenticated) {
            return "/";
        }
        return SkysailRootApplication.LOGIN_PATH;
    }

}
