package io.skysail.domain.core.resources;

import io.skysail.domain.Identifiable;
import lombok.Getter;

public class PostResource<T extends Identifiable> extends Resource<T> {

	@Getter
	private Class<?> resourceClass;

	public PostResource(Class<?> resourceClass) {
		this.resourceClass = resourceClass;
	}


}
