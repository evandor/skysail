package io.skysail.server.app.ref.singleentity.resources;

import java.security.Principal;
import java.util.List;

import org.restlet.data.Status;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.ref.singleentity.Account;
import io.skysail.server.app.ref.singleentity.SingleEntityApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

public class AccountResource extends EntityServerResource<Account> {

    private String id;
    private SingleEntityApplication app;

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (SingleEntityApplication) getApplication();
    }

    @Override
    public SkysailResponse<Account> eraseEntity() {
        String owner = app.getRepo().findOne(id).getOwner();
        Principal principal = getApplication().getAuthenticationService().getPrincipal(getRequest());
        String username = principal.getName();
        if (!username.equals(owner)) {
            getResponse().setStatus(Status.CLIENT_ERROR_FORBIDDEN, "current user is not allowed to delete this entity.");
            return new SkysailResponse<>();
        }
        app.getRepo().delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutAccountResource.class, AccountResource.class);
    }

    @Override
    public Account getEntity() {
        return app.getRepo().findOne(id);
    }

}
