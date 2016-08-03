package io.skysail.server.app.reference.singleentity.resources;

import io.skysail.server.app.reference.singleentity.Account;
import io.skysail.server.app.reference.singleentity.SingleEntityApplication;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class PostAccountResource extends PostEntityServerResource<Account> {

    private SingleEntityApplication app;

    @Override
    public void doInit() {
        app = (SingleEntityApplication) getApplication();
    }

    @Override
    public Account createEntityTemplate() {
        return new Account();
    }

    @Override
    public void addEntity(Account entity) {
        String id = app.getRepository(Account.class).save(entity, app.getApplicationModel()).toString();
        entity.setId(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(AccountsResource.class);
    }

}
