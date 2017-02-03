package io.skysail.server.app.designer.test;

import java.util.Optional;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.Entity;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbService;

public class TestRepository implements DbRepository {

    private DbService dbService;

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
    }

    @Override
    public Class<? extends Entity> getRootEntity() {
        return null;
    }

    @Override
    public Entity findOne(String id) {
        return null;
    }

    @Override
    public Object save(Entity identifiable, ApplicationModel appModel) {
        return null;
    }


    @Override
    public void delete(Entity identifiable) {
    }

    @Override
    public Object update(Entity entity, ApplicationModel applicationModel) {
        return null;
    }

    @Override
    public Optional<Entity> findOne(String identifierKey, String id) {
        // TODO Auto-generated method stub
        return null;
    }

}
