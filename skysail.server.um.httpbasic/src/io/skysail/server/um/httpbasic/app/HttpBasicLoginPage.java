package io.skysail.server.um.httpbasic.app;

import org.restlet.data.Form;
import org.restlet.resource.Get;

import de.twenty11.skysail.server.um.domain.Credentials;
import io.skysail.api.responses.FormResponse;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class HttpBasicLoginPage extends PostEntityServerResource<Credentials> {
	
    private HttpBasicUmApplication app;
    
	@Override
    protected void doInit() {
        app = (HttpBasicUmApplication) getApplication();
    }

    @Get("htmlform")
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
