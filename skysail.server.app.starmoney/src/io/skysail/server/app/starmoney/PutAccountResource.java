package io.skysail.server.app.starmoney;

import java.util.List;

import org.restlet.resource.ResourceException;

import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.ext.starmoney.domain.DbAccount;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutAccountResource extends PutEntityServerResource<Account> {

    protected String id;
    protected StarMoneyApplication app;
    private Account account;
    private DbAccountRepository dbRepo;

    @Override
    protected void doInit() throws ResourceException {
        id = getAttribute("id");
        app = (StarMoneyApplication)getApplication();
        account = app.getAccount(getAttribute("id"));
        dbRepo = (DbAccountRepository) app.getDbRepo();
    }

    @Override
    public void updateEntity(Account  entity) {
        Filter filter = new Filter("(&(kontonummer="+account.getKontonummer()+")(bankleitzahl="+account.getBankleitzahl()+"))");
        List<DbAccount> accounts = dbRepo.find(filter);
        if (accounts.isEmpty()) {
            DbAccount dbAccount = new DbAccount(account, entity.getName());
            dbRepo.save(dbAccount, getApplicationModel());
        } else {
            DbAccount dbAccount = new DbAccount(account, entity.getName());
            dbAccount.setId(accounts.get(0).getId());
            dbRepo.update(dbAccount,app.getApplicationModel());
        }
    }

    @Override
    public Account getEntity() {
        return account;
    }

    @Override
    public String redirectTo() {
        return super.redirectTo(AccountsResource.class);
    }
}
