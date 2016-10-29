package io.skysail.server.app.pline.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.pline.Registration;
import io.skysail.server.app.pline.PlineApplication;
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
        Registration original = getEntity();
        copyProperties(original,entity);

        app.getRepository(Registration.class).update(original,app.getApplicationModel());
    }

    @Override
    public Registration getEntity() {
        return (Registration)app.getRepository(Registration.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(RegistrationsResource.class);
    }
}