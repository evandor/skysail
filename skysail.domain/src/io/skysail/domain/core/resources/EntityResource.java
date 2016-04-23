package io.skysail.domain.core.resources;

import io.skysail.domain.Identifiable;
import lombok.Getter;

public class EntityResource<T extends Identifiable> extends Resource<T> {

	@Getter
	private Class<?> resourceClass;

	public EntityResource(Class<?> resourceClass) {
		this.resourceClass = resourceClass;
	}

}
