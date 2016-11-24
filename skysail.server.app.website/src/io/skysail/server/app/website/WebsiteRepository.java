package io.skysail.server.app.website;

import org.osgi.service.component.annotations.Component;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

@Component(immediate = true, property = "name=WebsiteRepository")
public class WebsiteRepository extends GraphDbRepository<Bookmark> implements DbRepository {

    public WebsiteRepository(DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(Bookmark.class));
        dbService.register(Bookmark.class);
    }

}