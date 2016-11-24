package io.skysail.server.app.starmoney.transactions;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.starmoney.StarMoneyApplication;
import io.skysail.server.app.starmoney.repos.DbAccountRepository;
import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.ext.starmoney.domain.Transaction;
import io.skysail.server.restlet.resources.ListServerResource;

public class AccountTransactionsPivotResource extends ListServerResource<Transaction> {

    private StarMoneyApplication app;
    private DbAccountRepository repo;

    public AccountTransactionsPivotResource() {
        //super(TodoListsTodoResource.class);
        addToContext(ResourceContextId.LINK_GLYPH, "list");
        addToContext(ResourceContextId.LINK_TITLE, "pivot");
    }

    @Override
    protected void doInit() {
        app = (StarMoneyApplication) getApplication();
        repo = (DbAccountRepository) app.getRepository(Account.class);
    }

    @Override
    public List<Transaction> getEntity() {
        Account account = app.getCvsRepo().findAll().stream().filter(a -> {
            //String theId = "#"+getAttribute("id");
            String theId = getAttribute("id");
            return a.getId().equals(theId);
        }).findFirst().orElse(new Account());
        List<Transaction> transactions = account.getTransactions();
//        String sql = "select * from " + DbClassName.of(Transaction.class) + " where #"+getAttribute("id")+" in IN(transactions)";
//        List<Transaction> transactions =  (List<Transaction>) repo.execute(Transaction.class, sql);
//        transactions.stream().forEach(t -> t.setSaldo(Math.round(t.getSaldo())));
        return transactions;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(AccountTransactionsResource.class, AccountTransactionsSaldoResource.class, AccountTransactionsPivotResource.class);
    }
}
