package io.skysail.server.app;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.EntityModel;
import io.skysail.server.domain.jvm.JavaEntityModel;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.NonNull;

public class EntityFactory {

    public static EntityModel createFrom(@NonNull Class<? extends Identifiable> entityClass, SkysailServerResource<?> resourceInstance) {
        return new JavaEntityModel(entityClass, resourceInstance);
    }

}
