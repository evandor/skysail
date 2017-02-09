package io.skysail.core.model;

import io.skysail.domain.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

public class SkysailApplicationModelTest {

    @Data
    private class EntitySupertype implements Entity {
        private String id;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    private class EntitySubtype extends EntitySupertype {
    }

//    @Test
//    public void entity_supertype_relation_is_added_to_model() {
//        SkysailServerResource<?> resourceClass = null;
//        SkysailEntityModel<EntitySupertype> supertypeEntity = new SkysailEntityModel<EntitySupertype>(
//                EntitySupertype.class, resourceClass);
//
//        SkysailEntityModel<EntitySubtype> subtypeEntity = new SkysailEntityModel<EntitySubtype>(
//                EntitySubtype.class, resourceClass);
//
//        SkysailApplicationModel app = new SkysailApplicationModel("app17");
//        app.addOnce(supertypeEntity);
//        app.addOnce(subtypeEntity);
//
//        assertThat(app.getEntitySupertype(EntitySubtype.class.getName()).getId(),
//                is(EntitySupertype.class.getName()));
//    }
}
