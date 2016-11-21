package io.skysail.server.app.starmoney;

import java.util.Optional;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.repos.Repository;

public class StarMoneyInMemoryRepository implements Repository {

    @Override
    public Class<? extends Identifiable> getRootEntity() {
        return null;
    }

    @Override
    public Identifiable findOne(String id) {
        return null;
    }

    @Override
    public Optional<Identifiable> findOne(String identifierKey, String id) {
        return null;
    }

    @Override
    public Object save(Identifiable identifiable, ApplicationModel applicationModel) {
        return null;
    }

    @Override
    public Object update(Identifiable entity, ApplicationModel applicationModel) {
        return null;
    }

    @Override
    public void delete(Identifiable identifiable) {
    }

}
