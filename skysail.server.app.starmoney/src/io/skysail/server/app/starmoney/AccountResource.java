package io.skysail.server.app.starmoney;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class AccountResource extends EntityServerResource<Account> {

    private String id;
    private StarMoneyApplication app;

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (StarMoneyApplication) getApplication();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Account getEntity() {
        return (Account) app.getRepository(Account.class).findOne(id);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutAccountResource.class,
            //,PostTodoListToNewTodoRelationResource.class,
            AccountsTransactionsResource.class
        );
    }


}
