package io.skysail.server.app;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.JavaEntityModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.NonNull;

public class EntityFactory {
	
	private EntityFactory() {}
	
    public static <T extends Identifiable> JavaEntityModel<T> createFrom(@NonNull Class<T> identifiable, SkysailServerResource<?> resourceInstance) {
        return new JavaEntityModel<>(identifiable, resourceInstance);
    }

}
