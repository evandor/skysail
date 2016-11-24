package io.skysail.server.app.pline.resources;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.pline.PlineApplication;
import io.skysail.server.app.pline.Registration;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostRegistrationResource extends PostEntityServerResource<Registration> {

	protected PlineApplication app;

    public PostRegistrationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "Create new registration");
    }

    @Override
    protected void doInit() {
        app = (PlineApplication) getApplication();
    }

    @Override
    public Registration createEntityTemplate() {
        return new Registration();
    }

    @Override
    public void addEntity(Registration entity) {
        entity.setEmail(entity.getEmail().replace("&#64;", "[at]"));
        Object save = app.getRepo().save(entity, app.getApplicationModel());
        //entity.setId(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(RegistrationsResource.class);
    }


}