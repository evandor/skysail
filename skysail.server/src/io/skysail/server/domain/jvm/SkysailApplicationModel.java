package io.skysail.server.domain.jvm;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.EntityModel;
import io.skysail.server.app.SkysailApplication;
import lombok.Getter;
import lombok.Setter;

public class SkysailApplicationModel extends ApplicationModel {

    private Map<EntityModel<? extends Identifiable>, EntityModel<? extends Identifiable>> supertypes = new HashMap<>();
    private Map<EntityModel<? extends Identifiable>, EntityModel<? extends Identifiable>> subtypes = new HashMap<>();

    @Getter
    @Setter
    private BundleContext bundleContext;

    @Getter
    private SkysailApplication skysailApplication;

    public SkysailApplicationModel(SkysailApplication skysailApplication) {
        super(skysailApplication.getName());
        this.skysailApplication = skysailApplication;
    }

    @Override
    public <T extends Identifiable> ApplicationModel addOnce(EntityModel<T> entityModel) {
        ApplicationModel appModel = super.addOnce(entityModel);
        checkEntitySupertypeRelations();
        return appModel;
    }

    public EntityModel<?> getEntitySupertype(String entityId) {
        EntityModel<? extends Identifiable> subEntity = getEntity(entityId);
        return supertypes.get(subEntity);
    }

    public EntityModel<?> getEntitySubtype(String entityId) {
        EntityModel<? extends Identifiable> superEntity = getEntity(entityId);
        return subtypes.get(superEntity);
    }

    private void checkEntitySupertypeRelations() {
        getEntityIds().stream().forEach(entityKey -> {
            for (String otherEntityKey : getEntityIds()) {
                if (!entityKey.equals(otherEntityKey)) {
                    SkysailEntityModel<? extends Identifiable> entityModel = (SkysailEntityModel<? extends Identifiable>) getEntity(
                            entityKey);
                    SkysailEntityModel<? extends Identifiable> otherEntityModel = (SkysailEntityModel<? extends Identifiable>) getEntity(
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
