package io.skysail.server.app.portal;


import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

public class PortalRepository extends GraphDbRepository<Bookmark> implements DbRepository {

    public PortalRepository (DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(Bookmark.class));
        dbService.register(Bookmark.class);
    }

}