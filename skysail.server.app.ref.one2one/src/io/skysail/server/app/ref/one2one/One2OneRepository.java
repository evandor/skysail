package io.skysail.server.app.ref.one2one;

import org.osgi.service.component.annotations.Component;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

@Component(immediate = true, property = "name=One2OneRepository")
public class One2OneRepository extends GraphDbRepository<Master> implements DbRepository {

    public One2OneRepository(DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(Master.class));
        dbService.register(Master.class);
    }

}