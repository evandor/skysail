package io.skysail.core.model;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import io.skysail.core.model.SkysailEntityModel;
import io.skysail.domain.Entity;
import io.skysail.domain.core.EntityModel;

import io.skysail.server.domain.jvm.test.AThing;

public class SkysailEntityModelTest {

    private AThing aThing;
    private Class<? extends Entity> identifiableClass;

    @Before
    public void setUp() throws Exception {
        aThing = new AThing();
        identifiableClass = AThing.class;
    }

    @Test
    public void id_is_set_in_string_constructor() {
        EntityModel entity = new EntityModel(aThing.getClass().getSimpleName());
        assertThat(entity.getId(),is("AThing"));
    }

    @Test
    public void id_is_set_in_class_constructor() {
        Class<? extends Entity> cls = AThing.class;
        SkysailEntityModel entity = new SkysailEntityModel(null, cls, null);
        assertThat(entity.getId(),is(AThing.class.getName()));
    }

    @Test
    public void simple_stringField_is_detected_correctly() throws Exception {
        SkysailEntityModel entity = new SkysailEntityModel(null, identifiableClass, null);
        assertThat(entity.getFields().size(), is(1));
        //assertThat(entity.getFields().get("stringField").getId(),is("stringField"));
    }

    @Test
    public void entity_without_tab_definition_does_not_use_tabs() {
        SkysailEntityModel entity = new SkysailEntityModel(null, identifiableClass, null);
        assertThat(entity.getTabs().size(),is(0));
    }
// FIXME
//    @Test
//    public void entity_with_incomplete_tab_definition_uses_tabs_and_additional_default_tab() {
//        ClassEntityModel entity = new ClassEntityModel(AThingWithSingleTabDefinition.class);
//        assertThat(entity.getTabs().size(),is(2));
//        assertThat(entity.getTabs().get(0).getName(),is("theTab"));
//        assertThat(entity.getTabs().get(1).getName(),is("more..."));
//    }
//
//    @Test
//    public void entity_with_complete_tab_definition_uses_defined_tabs() {
//        ClassEntityModel entity = new ClassEntityModel(AThingWithCompleteTabDefinition.class);
//        assertThat(entity.getTabs().size(),is(2));
//        assertThat(entity.getTabs().get(0).getName(),is("theTab"));
//        assertThat(entity.getTabs().get(1).getName(),is("optional"));
//    }

}
