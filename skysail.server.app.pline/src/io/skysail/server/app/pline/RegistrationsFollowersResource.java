package io.skysail.server.app.pline;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.pline.resources.PostConfirmationResource;
import io.skysail.server.restlet.resources.ListServerResource;

public class RegistrationsFollowersResource extends ListServerResource<Registration> {

    private PlineApplication app;
    private PlineRepository repo;

    public RegistrationsFollowersResource() {
        addToContext(ResourceContextId.LINK_GLYPH, "list");
        addToContext(ResourceContextId.LINK_TITLE, "list followers for this account");
    }

    @Override
    protected void doInit() {
        app = (PlineApplication) getApplication();
        repo = (PlineRepository) app.getRepository(Registration.class);
    }

    @Override
    public List<Registration> getEntity() {
        Registration registration = repo.findOne(getAttribute("id"));
        return registration.getFollowers();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(RegistrationsFollowersResource.class, PostConfirmationResource.class);
    }
}
