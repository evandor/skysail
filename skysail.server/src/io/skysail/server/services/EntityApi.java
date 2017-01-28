package io.skysail.server.services;

import io.skysail.domain.Entity;

public interface EntityApi<T extends Entity> {
    
    Class<? extends Entity> getEntityClass();

    T create();
    
    void persist(T entity);
}
