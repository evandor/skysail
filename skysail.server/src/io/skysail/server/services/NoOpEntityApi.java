package io.skysail.server.services;

import io.skysail.domain.GenericIdentifiable;
import io.skysail.domain.Identifiable;

public class NoOpEntityApi implements EntityApi<Identifiable> {

    @Override
    public Class<? extends Identifiable> getEntityClass() {
        throw new IllegalStateException("API not available");
    }

    @Override
    public GenericIdentifiable create() {
        throw new IllegalStateException("API not available");
    }

	@Override
	public void persist(Identifiable entity) {
        throw new IllegalStateException("API not available");
	}

}
