package io.skysail.server.app.ref.singleentity.resources;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.ref.singleentity.Account;
import io.skysail.server.app.ref.singleentity.SingleEntityApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutAccountResource extends PutEntityServerResource<Account> {

    protected String id;
    protected SingleEntityApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (SingleEntityApplication)getApplication();
    }

    @Override
    public void updateEntity(Account  entity) {
        Account original = getEntity();
        copyProperties(original,entity);

        app.getRepo().update(original,app.getApplicationModel());
    }

    @Override
    public Account getEntity() {
        return app.getRepo().findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(AccountsResource.class);
    }
}
