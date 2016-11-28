package io.skysail.server.app.ref.singleentity;


import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class SingleEntityRepository extends GraphDbRepository<Account> implements DbRepository {

    public SingleEntityRepository(DbService dbService) {
    	this.dbService = dbService;
    	activate();
	}

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(Account.class));
        dbService.register(Account.class);
    }

}