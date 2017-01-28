package io.skysail.server.repo.inmemory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import io.skysail.domain.Entity;
import io.skysail.domain.core.repos.Repository;

public class ReadonlyInMemoryRepository<T extends Entity> implements Repository {

    protected List<T> entities = new CopyOnWriteArrayList<>();

    public List<T> findAll() {
        return Collections.unmodifiableList(entities);
    }

    public T findOne(String attribute) {
        List<T> results = entities.stream().filter(e -> e.getId().equals(attribute)).collect(Collectors.toList());
        if (results.isEmpty()) {
            return null;
        } else if (results.size() == 1) {
            return results.get(0);
        } else {
            throw new IllegalStateException("too many records found for id '" + attribute + "'");
        }
    }

}
