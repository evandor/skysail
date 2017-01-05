package io.skysail.server.um.aws;

import org.restlet.data.Form;
import org.restlet.resource.Get;

import io.skysail.api.responses.FormResponse;
import io.skysail.server.app.SkysailRootApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.um.domain.Credentials;

public class AwsLoginPage extends PostEntityServerResource<Credentials> {
	
    private AwsUmApplication app;
    
	@Override
    protected void doInit() {
        app = (AwsUmApplication) getApplication();
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
