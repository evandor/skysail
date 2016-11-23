package io.skysail.server.repo.inmemory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.repos.InMemoryRepository;
import io.skysail.server.utils.ReflectionUtils;

public class ReadonlyInMemoryRepository<T extends Identifiable> implements InMemoryRepository {

    private List<T> entities = new CopyOnWriteArrayList<>();

    private Class<T> entityType;

    @SuppressWarnings("unchecked")
    public ReadonlyInMemoryRepository() {
        entityType = (Class<T>) ReflectionUtils.getParameterizedType(getClass());
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<Identifiable> getRootEntity() {
        return (Class<Identifiable>) entityType;
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
        entities.add((T)identifiable);
        return null;
    }

    @Override
    public Object update(Identifiable entity, ApplicationModel applicationModel) {
        return null;
    }

    @Override
    public void delete(Identifiable identifiable) {
    }

    public List<T> findAll() {
        return Collections.unmodifiableList(entities);
    }

}
