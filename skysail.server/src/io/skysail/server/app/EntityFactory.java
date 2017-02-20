package io.skysail.server.app;

import io.skysail.core.app.SkysailApplication;
import io.skysail.core.model.SkysailEntityModel;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import lombok.NonNull;

public class EntityFactory {

	private EntityFactory() {}

    public static <T extends Entity> SkysailEntityModel<T> createFrom(SkysailApplication skysailApplication, @NonNull Class<T> identifiable, SkysailServerResource<?> resourceInstance) {
        return new SkysailEntityModel<>(skysailApplication.getFacetsProvider(), identifiable, resourceInstance);
    }

}
