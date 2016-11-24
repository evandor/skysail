package io.skysail.server.app.starmoney.repos;

import io.skysail.server.ext.starmoney.domain.Account;
import io.skysail.server.repo.inmemory.ReadonlyInMemoryRepository;

public class AccountsInMemoryRepository extends ReadonlyInMemoryRepository<Account> {

    public void save(Account account) {
        entities.add(account);
    }


}
