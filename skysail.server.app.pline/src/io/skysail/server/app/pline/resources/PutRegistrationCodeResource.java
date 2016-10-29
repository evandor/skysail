package io.skysail.server.app.pline.resources;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.pline.PlineApplication;
import io.skysail.server.app.pline.RandomString;
import io.skysail.server.app.pline.Registration;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutRegistrationCodeResource extends PutEntityServerResource<Registration> {

    protected String id;
    protected PlineApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (PlineApplication)getApplication();
    }

    @Override
    public void updateEntity(Registration entity) {
        entity.setCode(new RandomString(6).nextString());
        entity.setValidUtil(Date.from(LocalDate.now().plusDays(7).atStartOfDay(ZoneOffset.systemDefault()).toInstant()));
        app.getRepository(Registration.class).update(entity,app.getApplicationModel());
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