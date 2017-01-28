package io.skysail.domain.core.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.skysail.domain.Entity;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.EntityModel;
import io.skysail.domain.core.FieldModel;
import lombok.Getter;
import lombok.Setter;

public class ApplicationModelTest {

	@Getter
	@Setter
    private class IdentifiableSupertype implements Entity {
    	private String id;
	}

    @Test
    public void simple_application_structure_can_be_created() {
    	EntityModel<IdentifiableSupertype> entityModel23 = new EntityModel<>("e23");
        ApplicationModel app = new ApplicationModel("app17")
            .addOnce(entityModel23.add(new FieldModel(entityModel23, "f23", String.class)))
            .addOnce(new EntityModel<IdentifiableSupertype>("e24"));

        assertThat(app.getName(),is("app17"));
        assertThat(app.getEntityIds().size(),is(2));
        assertThat(app.getEntity("e23").getId(),is("e23"));
        assertThat(app.getEntity("e24").getId(),is("e24"));
    }

    @Test
    public void same_entity_can_be_added_only_once() {
        ApplicationModel app = new ApplicationModel("app17")
            .addOnce(new EntityModel<IdentifiableSupertype>("e23"))
            .addOnce(new EntityModel<IdentifiableSupertype>("e23"));

        assertThat(app.getName(),is("app17"));
        assertThat(app.getEntityIds().size(),is(1));
        assertThat(app.getEntity("e23").getId(),is("e23"));
    }

    @Test
    public void toString_contains_main_details() {
        ApplicationModel app = new ApplicationModel("app56");
        assertThat(app.toString(), containsString("app56"));
    }

    @Test
    public void toString_is_formatted_nicely() {
    	EntityModel<IdentifiableSupertype> entityModel23 = new EntityModel<>("e23");
        ApplicationModel app = new ApplicationModel("app37")
                .addOnce(entityModel23.add(new FieldModel(entityModel23, "f23", String.class)))
                .addOnce(new EntityModel<IdentifiableSupertype>("e24"));

        String[] toString = app.toString().split("\n");

        int i=0;
        assertThat(toString[i++],is("ApplicationModel: app37"));
        assertThat(toString[i++],is("Entities: "));
        assertThat(toString[i++],is(" * EntityModel: id='e23', isAggregate=true"));
        assertThat(toString[i++],is("   Fields:"));
        assertThat(toString[i++],is("    - FieldModel(id=e23|f23, type=String, inputType=null)"));
        assertThat(toString[i++],is(""));
        assertThat(toString[i++],is(" * EntityModel: id='e24', isAggregate=true"));
    }
}
