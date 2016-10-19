package io.skysail.server.app.starmoney;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.starmoney.transactions.AccountsTransactionsResource;
import io.skysail.server.restlet.resources.EntityServerResource;

public class AccountResource extends EntityServerResource<Account> {

    private String id;
    private StarMoneyApplication app;
    private StarMoneyRepository repo;

    @Override
    protected void doInit() {
        id = getAttribute("id");
        app = (StarMoneyApplication) getApplication();
        repo = (StarMoneyRepository) app.getRepository(Account.class);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        repo.delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public Account getEntity() {
        return repo.findOne(id);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutAccountResource.class,
            //,PostTodoListToNewTodoRelationResource.class,
            AccountsTransactionsResource.class
        );
    }


}
