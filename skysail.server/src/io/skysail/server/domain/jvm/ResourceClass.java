package io.skysail.server.domain.jvm;

import io.skysail.core.resources.SkysailServerResource;
import lombok.Getter;

public class ResourceClass {

	@Getter
	private Class<? extends SkysailServerResource<?>> resourceClass;

	@SuppressWarnings("unchecked")
    public ResourceClass(SkysailServerResource<?> resource) {
		this.resourceClass = (Class<? extends SkysailServerResource<?>>) resource.getClass();
	}

}
