package io.skysail.server.services;

import io.skysail.domain.GenericIdentifiable;
import io.skysail.domain.Identifiable;

public class NoOpEntityApi implements EntityApi<GenericIdentifiable> {

    @Override
    public Class<? extends Identifiable> getEntityClass() {
        return null;
    }

    @Override
    public GenericIdentifiable create() {
        return null;
    }

	@Override
	public String persist(GenericIdentifiable entity) {
		return null;
	}

}
