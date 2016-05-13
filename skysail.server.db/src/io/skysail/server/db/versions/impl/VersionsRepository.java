package io.skysail.server.db.versions.impl;

import java.util.Collections;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = "name=VersionsRepository")
@Slf4j
public class VersionsRepository extends GraphDbRepository<ComponentDbVersion> implements DbRepository {

    @Activate
    public void activate() {
        log.debug("activating VersionsRepository");
        dbService.createWithSuperClass("V", DbClassName.of(ComponentDbVersion.class));
        dbService.register(ComponentDbVersion.class);
    }

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    public Object execute(String statement) {
        return dbService.executeUpdateVertex(statement, Collections.emptyMap());
    }

}
