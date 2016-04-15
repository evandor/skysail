package de.twenty11.skysail.server.resources;

import org.restlet.data.Form;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;

import de.twenty11.skysail.server.um.domain.Credentials;
import io.skysail.api.responses.FormResponse;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class HttpDigestLoginPage extends PostEntityServerResource<Credentials> {
	
    private SkysailRootApplication app;
    
	@Override
    protected void doInit() throws ResourceException {
        app = (SkysailRootApplication) getApplication();
    }

    @Get("htmlform")
    @Override
    public FormResponse<Credentials> createForm() {
        return new FormResponse<>(getResponse(), getEntity(), SkysailRootApplication.LOGIN_PATH);
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
        if (app.isAuthenticated(getRequest())) {
            return "/";
        }
        return SkysailRootApplication.LOGIN_PATH;
    }

}
