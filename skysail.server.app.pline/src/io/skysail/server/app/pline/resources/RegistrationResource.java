package io.skysail.server.app.pline.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.pline.PlineApplication;
import io.skysail.server.app.pline.Registration;
import io.skysail.server.app.pline.RegistrationsFollowersResource;
import io.skysail.server.restlet.resources.EntityServerResource;

public class RegistrationResource extends EntityServerResource<Registration> {

    private String id;
    private PlineApplication app;

    public RegistrationResource() {
        addToContext(ResourceContextId.LINK_TITLE, "details");
        addToContext(ResourceContextId.LINK_GLYPH, "search");
    }

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (PlineApplication) getApplication();
    }


    @Override
    public SkysailResponse<Registration> eraseEntity() {
    	app.getRepo().delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public Registration getEntity() {
        return (Registration)app.getRepository().findOne(id);
    }

	@Override
    public List<Link> getLinks() {
        return super.getLinks(PutRegistrationResource.class, RegistrationsFollowersResource.class, PutRegistrationCodeResource.class);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(RegistrationsResource.class);
    }


}