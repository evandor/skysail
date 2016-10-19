package io.skysail.server.app.starmoney;

import java.util.List;

import org.restlet.resource.ResourceException;

import io.skysail.api.links.Link;
import io.skysail.server.app.starmoney.transactions.TransactionsResource;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class AccountsResource extends ListServerResource<Account> {

    private StarMoneyRepository repo;

    public AccountsResource() {
        super(AccountResource.class);
    }

    @Override
    protected void doInit() throws ResourceException {
        repo = (StarMoneyRepository)getApplication().getRepository(Account.class);
    }

    @Override
    public List<?> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse());
        return repo.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TransactionsResource.class, AccountResource.class);
    }

}
