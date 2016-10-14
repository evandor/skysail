package io.skysail.server.app.starmoney.transactions;

import java.util.List;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.starmoney.StarMoneyRepository;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class TransactionsResource extends ListServerResource<Transaction> {

    private StarMoneyRepository repo;

    @Override
    protected void doInit() throws ResourceException {
        repo = (StarMoneyRepository)getApplication().getRepository(Transaction.class);
    }

    @Override
    public List<?> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse(), repo.count(filter));
        return repo.find(filter, pagination);
    }

}
