package io.skysail.server.app.starmoney.transactions;

import java.util.List;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.starmoney.repos.DbAccountRepository;
import io.skysail.server.ext.starmoney.domain.Transaction;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class TransactionsResource extends ListServerResource<Transaction> {

    private DbAccountRepository repo;

    @Override
    protected void doInit() throws ResourceException {
        repo = (DbAccountRepository)getApplication().getRepository(Transaction.class);
    }

    @Override
    public List<?> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse());
        return repo.find(filter, pagination);
    }

}
