package io.skysail.server.app.starmoney.transactions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.starmoney.StarMoneyApplication;
import io.skysail.server.app.starmoney.StarMoneyRepository;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.ext.starmoney.domain.Transaction;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.sorting.Sorting;
import io.skysail.server.restlet.resources.ListServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountsTransactionsResource extends ListServerResource<Transaction> {

    private StarMoneyApplication app;
    private StarMoneyRepository repo;
    private Account account;

    public AccountsTransactionsResource() {
        addToContext(ResourceContextId.LINK_GLYPH, "list");
        addToContext(ResourceContextId.LINK_TITLE, "list transactions for this account");
    }

    @Override
    protected void doInit() {
        app = (StarMoneyApplication) getApplication();
        repo = (StarMoneyRepository) app.getRepository(Account.class);
        account = app.getAccount(getAttribute("id"));
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

//    // @Override
//    public List<Transaction> getEntityFromDB() {
//        Filter filter = new Filter(getRequest(), getFacetsFor(Transaction.class));
//        Pagination pagination = new Pagination(getRequest(), getResponse());
//        Sorting sorting = new Sorting(getRequest());
//        List<Transaction> transactions = repo.find(Transaction.class, "#" + getAttribute("id") + " in IN(transactions)",
//                filter, sorting, pagination);
//        handleFacets(transactions, getApplicationModel());
//        return transactions;
//    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(AccountsTransactionsResource.class, AccountsTransactionsSaldoResource.class, AccountsTransactionsPivotResource2.class);
    }
}
