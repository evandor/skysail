package io.skysail.server.app.starmoney.repos;


import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;
import io.skysail.server.ext.starmoney.domain.DbAccount;
import io.skysail.server.ext.starmoney.domain.DbTransaction;

//@Component(immediate = true, property = "name=DbAccountRepository")
public class DbAccountRepository extends GraphDbRepository<DbAccount> implements DbRepository {

    public DbAccountRepository(DbService dbService) {
        this.dbService = dbService;
        activate();
    }


    private void activate() {
        dbService.createWithSuperClass("V",
                DbClassName.of(DbAccount.class),
                DbClassName.of(DbTransaction.class)
        );
        dbService.register(DbAccount.class,DbTransaction.class);
    }

}