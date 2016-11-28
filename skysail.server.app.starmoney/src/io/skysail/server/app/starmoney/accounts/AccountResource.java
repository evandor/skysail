package io.skysail.server.app.starmoney.accounts;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.starmoney.StarMoneyApplication;
import io.skysail.server.app.starmoney.repos.DbAccountRepository;
import io.skysail.server.app.starmoney.transactions.AccountTransactionsResource;
import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.ext.starmoney.domain.DbAccount;
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
        app.getDbRepo().delete(id);
        return new SkysailResponse<>();
    }

    @Override
    public Account getEntity() {
        return null;//repo.findOne(id);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(PutAccountResource.class,
            //,PostTodoListToNewTodoRelationResource.class,
            AccountTransactionsResource.class
        );
    }


}
