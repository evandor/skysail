package io.skysail.domain.core.resources;

import io.skysail.domain.Identifiable;
import lombok.Getter;

public class ListResource<T extends Identifiable> extends Resource<T> {

	@Getter
	private Class<?> resourceClass;

	public ListResource(Class<?> resourceClass) {
		this.resourceClass = resourceClass;
	}

}
