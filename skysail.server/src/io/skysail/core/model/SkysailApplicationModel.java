package io.skysail.core.model;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;

import io.skysail.domain.Entity;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.EntityModel;
import io.skysail.server.domain.jvm.SkysailEntityModel;
import lombok.Getter;
import lombok.Setter;

public class SkysailApplicationModel extends ApplicationModel {

    private Map<EntityModel<? extends Entity>, EntityModel<? extends Entity>> supertypes = new HashMap<>();
    private Map<EntityModel<? extends Entity>, EntityModel<? extends Entity>> subtypes = new HashMap<>();

    @Getter
    @Setter
    private BundleContext bundleContext;

    public SkysailApplicationModel(String name) {
        super(name);
    }

    @Override
    public <T extends Entity> ApplicationModel addOnce(EntityModel<T> entityModel) {
        ApplicationModel appModel = super.addOnce(entityModel);
        checkEntitySupertypeRelations();
        return appModel;
    }

    public EntityModel<?> getEntitySupertype(String entityId) {
        EntityModel<? extends Entity> subEntity = getEntity(entityId);
        return supertypes.get(subEntity);
    }

    public EntityModel<?> getEntitySubtype(String entityId) {
        EntityModel<? extends Entity> superEntity = getEntity(entityId);
        return subtypes.get(superEntity);
    }

    private void checkEntitySupertypeRelations() {
        getEntityIds().stream().forEach(entityKey -> {
            for (String otherEntityKey : getEntityIds()) {
                if (!entityKey.equals(otherEntityKey)) {
                    SkysailEntityModel<? extends Entity> entityModel = (SkysailEntityModel<? extends Entity>) getEntity(
                            entityKey);
                    SkysailEntityModel<? extends Entity> otherEntityModel = (SkysailEntityModel<? extends Entity>) getEntity(
                            otherEntityKey);
                    if (entityModel.getIdentifiableClass().isAssignableFrom(otherEntityModel.getIdentifiableClass())) {
                        supertypes.put(otherEntityModel, entityModel);
                        subtypes.put(entityModel, otherEntityModel);
                    }
                }
            }
        });
    }

}
