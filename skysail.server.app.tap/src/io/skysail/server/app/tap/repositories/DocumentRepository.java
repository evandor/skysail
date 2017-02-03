package io.skysail.server.app.tap.repositories;

import io.skysail.domain.core.repos.DbRepository;
import  io.skysail.server.app.tap.Document;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class DocumentRepository extends GraphDbRepository<Document> implements DbRepository {

    public DocumentRepository (DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(Document.class));
        dbService.register(Document.class);
    }

}
