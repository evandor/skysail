package io.skysail.server.app.starmoney;


import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

/**
 * generated from repository.stg
 */
@Component(immediate = true, property = "name=StarMoneyRepository")
public class StarMoneyRepository extends GraphDbRepository<Account> implements DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V",
                DbClassName.of(Account.class),
                DbClassName.of(Transaction.class)
        );
        dbService.register(Account.class,Transaction.class);
    }

}