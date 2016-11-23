package io.skysail.server.app.starmoney.repos;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import io.skysail.domain.core.repos.Repository2;
import io.skysail.server.ext.starmoney.domain.Account;

//@Component(immediate = true, property = "name=AccountsInMemoryRepository")
public class AccountsInMemoryRepository implements Repository2 {

    private List<Account> entities = new CopyOnWriteArrayList<>();

    public void save(Account account) {
        entities.add(account);
    }

    public List<Account> findAll() {
        return Collections.unmodifiableList(entities);
    }

    public Account findOne(String attribute) {
        List<Account> results = entities.stream().filter(e -> e.getId().equals(attribute)).collect(Collectors.toList());
        if (results.size() == 0) {
            return null;
        } else if (results.size() == 1) {
            return results.get(0);
        } else {
            throw new IllegalStateException("too many records found for id '" + attribute + "'");
        }
    }
}
