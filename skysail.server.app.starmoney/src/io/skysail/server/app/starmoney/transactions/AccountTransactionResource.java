package io.skysail.server.app.starmoney.transactions;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.starmoney.StarMoneyApplication;
import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.ext.starmoney.domain.Transaction;
import io.skysail.server.restlet.resources.EntityServerResource;

public class AccountTransactionResource extends EntityServerResource<Transaction> {

    private StarMoneyApplication app;
    private Account account;
    private String starmoneyId;

    public AccountTransactionResource() {
        addToContext(ResourceContextId.LINK_GLYPH, "search");
        addToContext(ResourceContextId.LINK_TITLE, "transaction details");
    }

    @Override
    protected void doInit() {
        app = (StarMoneyApplication) getApplication();
        starmoneyId = getAttribute("starmoneyId");
    }

    @Override
    public Transaction getEntity() {
        return account.getTransactions().stream()
                .filter(t -> starmoneyId.equals(t.getStarMoneyId()))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(AccountTransactionResource.class);
    }
}
