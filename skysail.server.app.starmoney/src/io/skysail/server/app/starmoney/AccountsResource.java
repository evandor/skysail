package io.skysail.server.app.starmoney;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.starmoney.transactions.TransactionsResource;
import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.ext.starmoney.domain.DbAccount;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

public class AccountsResource extends ListServerResource<Account> {

    private StarMoneyRepository repo;

    public AccountsResource() {
        super(AccountResource.class);
    }

    @Override
    protected void doInit() {
        repo = (StarMoneyRepository)getApplication().getRepository(DbAccount.class);
    }

    @Override
    public List<?> getEntity() {
         List<Account> accountsFromFiles = Import2MemoryProcessor.getAccounts();
         accountsFromFiles.stream().forEach(account -> {
             Filter filter = new Filter("(&(kontonummer="+account.getKontonummer()+")(bankleitzahl="+account.getBankleitzahl()+"))");
             List<DbAccount> dbAccounts = repo.find(filter);
             if (!dbAccounts.isEmpty()) {
                 account.setName(dbAccounts.get(0).getName());
             }
         });
         return accountsFromFiles;
//        Filter filter = new Filter(getRequest());
//        Pagination pagination = new Pagination(getRequest(), getResponse());
//        return repo.find(filter, pagination);
    }

    @Override
    public List<Link> getLinks() {
        return super.getLinks(TransactionsResource.class, AccountResource.class);
    }

}
