package io.skysail.server.app.ref.fields.repositories;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.ref.fields.domain.TextEntity;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class TextEntityRepository extends GraphDbRepository<TextEntity> implements DbRepository {

    public TextEntityRepository (DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(TextEntity.class));
        dbService.register(TextEntity.class);
    }

}
