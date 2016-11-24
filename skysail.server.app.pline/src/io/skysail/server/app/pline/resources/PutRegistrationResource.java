package io.skysail.server.app.pline.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.pline.PlineApplication;
import io.skysail.server.app.pline.Registration;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutRegistrationResource extends PutEntityServerResource<Registration> {

    protected String id;
    protected PlineApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (PlineApplication)getApplication();
    }

    @Override
    public void updateEntity(Registration  entity) {
        Registration original = new Registration();
        copyProperties(original,entity);

        app.getRepo().update(original,app.getApplicationModel());
    }

    @Override
    public Registration getEntity() {
        return app.getRepo().findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(RegistrationsResource.class);
    }

}