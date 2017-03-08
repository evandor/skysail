package io.skysail.app.instagram.mocked.repositories;

import io.skysail.domain.core.repos.DbRepository;
import  io.skysail.app.instagram.mocked.InstagramUser;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class InstagramUserRepository extends GraphDbRepository<InstagramUser> implements DbRepository {

    public InstagramUserRepository (DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(InstagramUser.class));
        dbService.register(InstagramUser.class);
    }

}
