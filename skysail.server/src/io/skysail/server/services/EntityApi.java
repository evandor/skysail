package io.skysail.server.services;

import io.skysail.domain.Identifiable;

public interface EntityApi<T extends Identifiable> {
    
    Class<? extends Identifiable> getEntityClass();

    T create();
    
    void perist();
}
