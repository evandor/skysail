package io.skysail.server.app.starmoney.transactions;

import java.util.List;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.starmoney.StarMoneyApplication;
import io.skysail.server.ext.starmoney.domain.Transaction;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class TransactionsResource extends ListServerResource<Transaction> {

    private StarMoneyApplication app;

	@Override
    protected void doInit() throws ResourceException {
    	app = (StarMoneyApplication)getApplication();
    }

    @Override
    public List<Transaction> getEntity() {
        Filter filter = new Filter(getRequest());
        Pagination pagination = new Pagination(getRequest(), getResponse());
        return null;//app.getDbRepo().find(filter, pagination);
    }

}
