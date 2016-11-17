package io.skysail.server.app.starmoney;


import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;
import io.skysail.server.ext.starmoney.domain.DbAccount;
import io.skysail.server.ext.starmoney.domain.DbTransaction;

/**
 * generated from repository.stg
 */
@Component(immediate = true, property = "name=StarMoneyRepository")
public class StarMoneyRepository extends GraphDbRepository<DbAccount> implements DbRepository {

    @Override
    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V",
                DbClassName.of(DbAccount.class),
                DbClassName.of(DbTransaction.class)
        );
        dbService.register(DbAccount.class,DbTransaction.class);
    }

}