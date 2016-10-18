package io.skysail.server.app;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.SkysailEntityModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.NonNull;

public class EntityFactory {

	private EntityFactory() {}

    public static <T extends Identifiable> SkysailEntityModel<T> createFrom(SkysailApplication skysailApplication, @NonNull Class<T> identifiable, SkysailServerResource<?> resourceInstance) {
        return new SkysailEntityModel<>(skysailApplication, identifiable, resourceInstance);
    }

}
