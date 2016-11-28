package io.skysail.server.app.pline;


import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class PlineRepository extends GraphDbRepository<Registration> implements DbRepository {

    public PlineRepository(DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(Registration.class));
        dbService.register(Registration.class);
    }

}