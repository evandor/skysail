package io.skysail.server.app.starmoney.accounts;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.starmoney.StarMoneyApplication;
import io.skysail.server.app.starmoney.repos.DbAccountRepository;
import io.skysail.server.app.starmoney.transactions.TransactionsResource;
import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.ext.starmoney.domain.DbAccount;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

public class AccountsResource extends ListServerResource<Account> {

    private DbAccountRepository dbRepo;
    private StarMoneyApplication app;

    public AccountsResource() {
        super(AccountResource.class);
    }

    @Override
    protected void doInit() {
        app = (StarMoneyApplication)getApplication();
        dbRepo = app.getDbRepo();
    }

    /**
     * reads all accounts from csv repository and augments their names with data from the db repo if available.
     */
    @Override
    public List<Account> getEntity() {
         List<Account> csvAccounts = app.getCvsRepo().findAll();
         csvAccounts.stream().forEach(account -> {
             Filter filter = new Filter("(&(kontonummer="+account.getKontonummer()+")(bankleitzahl="+account.getBankleitzahl()+"))");
             List<DbAccount> dbAccounts = dbRepo.find(filter);
             if (!dbAccounts.isEmpty()) {
                 account.setName(dbAccounts.get(0).getName());
             }
         });
         return csvAccounts;
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TransactionsResource.class, AccountResource.class);
    }

}
