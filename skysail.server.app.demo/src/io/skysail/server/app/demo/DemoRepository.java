package io.skysail.server.app.demo;


import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

//@Component(immediate = true, property = "name=DemoRepository")
public class DemoRepository extends GraphDbRepository<Bookmark> implements DbRepository {

    public DemoRepository(DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(Bookmark.class));
        dbService.register(Bookmark.class);
    }

}