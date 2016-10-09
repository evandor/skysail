package io.skysail.server.app.starmoney;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.db.DbClassName;
import io.skysail.server.restlet.resources.ListServerResource;

public class AccountsTransactionsResource extends ListServerResource<Transaction> {

    private StarMoneyApplication app;
    private StarMoneyRepository repo;

    public AccountsTransactionsResource() {
        //super(TodoListsTodoResource.class);
        addToContext(ResourceContextId.LINK_GLYPH, "list");
        addToContext(ResourceContextId.LINK_TITLE, "list courses for this timetable");
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
        return (List<Transaction>) repo.execute(Transaction.class, sql);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(AccountsTransactionsResource.class);
    }
}
