package io.skysail.server.app.ref.fields.repositories;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.ref.fields.domain.PasswordEntity;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class PasswordEntityRepository extends GraphDbRepository<PasswordEntity> implements DbRepository {

    public PasswordEntityRepository (DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(PasswordEntity.class));
        dbService.register(PasswordEntity.class);
    }

}
