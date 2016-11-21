package io.skysail.server.app.starmoney.transactions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.starmoney.StarMoneyApplication;
import io.skysail.server.app.starmoney.StarMoneyDbRepository;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.ext.starmoney.domain.Transaction;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

public class AccountTransactionsPivotResource2 extends ListServerResource<Transaction> {

    private StarMoneyApplication app;
    private StarMoneyDbRepository repo;
    private Account account;

    public AccountTransactionsPivotResource2() {
        addToContext(ResourceContextId.LINK_GLYPH, "list");
        addToContext(ResourceContextId.LINK_TITLE, "pivot2");
    }

    @Override
    protected void doInit() {
        app = (StarMoneyApplication) getApplication();
        repo = (StarMoneyDbRepository) app.getRepository(Account.class);
        account = app.getAccount(getAttribute("id"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Transaction> getEntity() {
//        Account account = Import2MemoryProcessor.getAccounts().stream().filter(a -> {
//            //String theId = "#"+getAttribute("id");
//            String theId = getAttribute("id");
//            return a.getId().equals(theId);
//        }).findFirst().orElse(new Account());
//        List<Transaction> transactions = account.getTransactions();
////        String sql = "select * from " + DbClassName.of(Transaction.class) + " where #"+getAttribute("id")+" in IN(transactions)";
////        List<Transaction> transactions =  (List<Transaction>) repo.execute(Transaction.class, sql);
////        transactions.stream().forEach(t -> t.setSaldo(Math.round(t.getSaldo())));
//        return transactions;
        List<Transaction> transactions = account.getTransactions();
        handleFacets(Transaction.class, transactions, getApplicationModel());
        Filter filter = new Filter(getRequest(), getFacetsFor(Transaction.class));
        return applyFilter(filter, transactions);
    }

    private List<Transaction> applyFilter(Filter filter, List<Transaction> transactions) {
        if (filter == null || filter.getParams().size() == 0) {
            return transactions;
        }
        if (transactions.isEmpty()) {
            return transactions;
        }
        Map<String, FieldFacet> theFacets = getFacetsFor(Transaction.class);
        return transactions.stream().filter(t -> filter.evaluateEntity(t, theFacets)).collect(Collectors.toList());
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(AccountTransactionsResource.class, AccountTransactionsSaldoResource.class, AccountTransactionsPivotResource2.class);
    }
}
