package io.skysail.server.domain.jvm.test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.*;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.EntityModel;
import io.skysail.server.domain.jvm.JavaEntityModel;

public class JavaEntityModelTest {

    private AThing aThing;
    private Class<? extends Identifiable> identifiableClass;

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
        Class<? extends Identifiable> cls = AThing.class;
        JavaEntityModel entity = new JavaEntityModel(cls, null);
        assertThat(entity.getId(),is(AThing.class.getName()));
    }

    @Test
    public void simple_stringField_is_detected_correctly() throws Exception {
        JavaEntityModel entity = new JavaEntityModel(identifiableClass, null);
        assertThat(entity.getFields().size(), is(1));
        //assertThat(entity.getFields().get("stringField").getId(),is("stringField"));
    }

    @Test
    public void entity_without_tab_definition_does_not_use_tabs() {
        JavaEntityModel entity = new JavaEntityModel(identifiableClass, null);
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
