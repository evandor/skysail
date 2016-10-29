package io.skysail.server.app.starmoney;

import org.restlet.resource.ResourceException;

import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutAccountResource extends PutEntityServerResource<Account> {

    protected String id;
    protected StarMoneyApplication app;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (StarMoneyApplication)getApplication();
    }

    @Override
    public void updateEntity(Account  entity) {
        Account original = getEntity();
        copyProperties(original,entity);
        app.getRepository(Account.class).update(original,app.getApplicationModel());
    }

    @Override
    public Account getEntity() {
        return (Account)app.getRepository(Account.class).findOne(id);
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(AccountsResource.class);
    }
}
