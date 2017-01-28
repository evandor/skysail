package io.skysail.server.services;

import io.skysail.domain.GenericIdentifiable;
import io.skysail.domain.Entity;

public class NoOpEntityApi implements EntityApi<Entity> {

    @Override
    public Class<? extends Entity> getEntityClass() {
        throw new IllegalStateException("API not available");
    }

    @Override
    public GenericIdentifiable create() {
        throw new IllegalStateException("API not available");
    }

	@Override
	public void persist(Entity entity) {
        throw new IllegalStateException("API not available");
	}

}
