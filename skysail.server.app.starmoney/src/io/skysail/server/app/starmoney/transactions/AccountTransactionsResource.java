package io.skysail.server.app.starmoney.transactions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.starmoney.StarMoneyApplication;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.ext.starmoney.domain.Transaction;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.sorting.Sorting;
import io.skysail.server.restlet.resources.ListServerResource;

public class AccountTransactionsResource extends ListServerResource<Transaction> {

    private StarMoneyApplication app;
    private Account account;

    public AccountTransactionsResource() {
        super(AccountTransactionResource.class);
        addToContext(ResourceContextId.LINK_GLYPH, "list");
        addToContext(ResourceContextId.LINK_TITLE, "list transactions for this account");
    }

    @Override
    protected void doInit() {
        app = (StarMoneyApplication) getApplication();
        account = app.getCvsRepo().findOne(getAttribute("id"));
    }

    @Override
    public List<Transaction> getEntity() {
        List<Transaction> transactions = account.getTransactions();
        handleFacets(Transaction.class, transactions, getApplicationModel());
        Filter filter = new Filter(getRequest(), getFacetsFor(Transaction.class));
        Sorting sorting = new Sorting(getRequest());
        return applyFilter(filter, sorting, transactions);
    }

    private List<Transaction> applyFilter(Filter filter, Sorting sorting, List<Transaction> transactions) {
        if (filter == null || filter.getParams().size() == 0) {
            return transactions.stream()
                    .sorted(sorting.getComparator(Transaction.class))
                    .collect(Collectors.toList());
        }
        if (transactions.isEmpty()) {
            return transactions;
        }
        Map<String, FieldFacet> theFacets = getFacetsFor(Transaction.class);
        return transactions.stream()
                .filter(t -> filter.evaluateEntity(t, theFacets))
                .sorted(sorting.getComparator(Transaction.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(AccountTransactionsResource.class, AccountTransactionsSaldoResource.class, AccountTransactionsPivotResource2.class);
    }
}
