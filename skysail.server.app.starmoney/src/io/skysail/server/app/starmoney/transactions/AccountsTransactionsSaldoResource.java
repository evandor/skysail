package io.skysail.server.app.starmoney.transactions;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.starmoney.Account;
import io.skysail.server.app.starmoney.StarMoneyApplication;
import io.skysail.server.app.starmoney.StarMoneyRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;

public class AccountsTransactionsSaldoResource extends ListServerResource<Transaction> {

    private StarMoneyApplication app;
    private StarMoneyRepository repo;

    public AccountsTransactionsSaldoResource() {
        //super(TodoListsTodoResource.class);
        addToContext(ResourceContextId.LINK_GLYPH, "list");
        addToContext(ResourceContextId.LINK_TITLE, "chart");
    }

    @Override
    protected void doInit() {
        app = (StarMoneyApplication) getApplication();
        repo = (StarMoneyRepository) app.getRepository(Account.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Transaction> getEntity() {
        String sql = "select * from " + DbClassName.of(Transaction.class) + " where #"+getAttribute("id")+" in IN(transactions)";
        List<Transaction> result =  (List<Transaction>) repo.execute(Transaction.class, sql);
        result.stream().forEach(t -> t.setSaldo(Math.round(t.getSaldo())));
        return result;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(AccountsTransactionsResource.class, AccountsTransactionsSaldoResource.class);
    }
}
