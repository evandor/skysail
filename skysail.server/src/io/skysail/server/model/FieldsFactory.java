package io.skysail.server.model;

import io.skysail.api.responses.*;
import io.skysail.core.app.SkysailApplicationService;
import io.skysail.core.resources.SkysailServerResource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FieldsFactory {

    private static SkysailApplicationService appService;

	public static FieldFactory getFactory(SkysailServerResource<?> resource) {
		Object entity = resource.getEntity();
        if (entity == null) {
            return new NoFieldFactory();
        }
        if (entity instanceof List) {
            return new DefaultListFieldFactory();
        }
        return new DefaultEntityFieldFactory(entity.getClass());
    }

    public static FieldFactory getFactory(SkysailResponse<?> response) {
        if (response.getEntity() == null) {
            return new NoFieldFactory();
        }
        if (response.getEntity() instanceof List) {
            return new DefaultListFieldFactory();
        } else if (response instanceof ConstraintViolationsResponse) {
            return entityFactory((ConstraintViolationsResponse<?>) response);
        } else if (response instanceof FormResponse) {
            return entityFactoryForForm((FormResponse<?>) response);
        } else {
            return new DefaultEntityFieldFactory(response.getEntity().getClass());
        }
    }

    private static FieldFactory entityFactory(ConstraintViolationsResponse<?> source) {
        Object entity = ((SkysailResponse<?>) source).getEntity();
        return new SkysailResponseEntityFieldFactory(source, entity.getClass());
    }

    private static FieldFactory entityFactoryForForm(FormResponse<?> source) {
        return new FormResponseEntityFieldFactory(source.getEntity().getClass());
    }

}
