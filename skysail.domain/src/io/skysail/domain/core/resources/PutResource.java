package io.skysail.domain.core.resources;

import io.skysail.domain.Identifiable;
import lombok.Getter;

public class PutResource<T extends Identifiable> extends Resource<T> {

	@Getter
	private Class<?> resourceClass;

	public PutResource(Class<?> resourceClass) {
		this.resourceClass = resourceClass;
	}

}
