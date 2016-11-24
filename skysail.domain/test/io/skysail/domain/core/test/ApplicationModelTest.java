package io.skysail.domain.core.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import io.skysail.domain.Identifiable;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.EntityModel;
import io.skysail.domain.core.FieldModel;
import lombok.Getter;
import lombok.Setter;

public class ApplicationModelTest {

	@Getter
	@Setter
    private class IdentifiableSupertype implements Identifiable {
    	private String id;
	}

    @Test
    public void simple_application_structure_can_be_created() {
        ApplicationModel app = new ApplicationModel("app17")
            .addOnce(new EntityModel<IdentifiableSupertype>("e23")
                    .add(new FieldModel("f23", String.class)))
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

//    @Test
//    public void associated_resources_are_collected_in_entityModel() {
//        EntityModel<IdentifiableSupertype> first = new EntityModel<IdentifiableSupertype>("e23");
//        first.setAssociatedListResource(String.class);
//
//        EntityModel<IdentifiableSupertype> second = new EntityModel<IdentifiableSupertype>("e23");
//        second.setAssociatedEntityResource(Integer.class);
//
//        EntityModel<IdentifiableSupertype> third = new EntityModel<IdentifiableSupertype>("e23");
//        third.setAssociatedPutResource(Character.class);
//
//        EntityModel<IdentifiableSupertype> fourth = new EntityModel<IdentifiableSupertype>("e23");
//        fourth.setAssociatedPostResource(Byte.class);
//
//        ApplicationModel app = new ApplicationModel("app17")
//            .addOnce(first)
//            .addOnce(second)
//            .addOnce(third)
//            .addOnce(fourth)
//            ;
//
//        assertThat(app.getEntity("e23").getAssociatedListResource().getResourceClass().toGenericString(),is(String.class.toGenericString()));
//        assertThat(app.getEntity("e23").getAssociatedEntityResource().getResourceClass().toGenericString(),is(Integer.class.toGenericString()));
//        assertThat(app.getEntity("e23").getAssociatedPutResource().getResourceClass().toGenericString(),is(Character.class.toGenericString()));
//        assertThat(app.getEntity("e23").getAssociatedPostResource().getResourceClass().toGenericString(),is(Byte.class.toGenericString()));
//    }
//
//    @Test
//    public void repositories_can_be_set_and_retrieved() {
//        ApplicationModel app = new ApplicationModel("app56");
//       // Repositories repos = new Repositories();
//        DbRepository aRepository = new DbRepository() {
//
//            @Override
//            public Object update(Identifiable entity, ApplicationModel model) {
//                return null;
//            }
//
//            @Override
//            public Object save(Identifiable identifiable, ApplicationModel appModel) {
//                return null;
//            }
//
//            @Override
//            public Class<? extends Identifiable> getRootEntity() {
//                return AThing.class;
//            }
//
//            @Override
//            public Identifiable findOne(String id) {
//                return null;
//            }
//
//            @Override
//            public void delete(Identifiable identifiable) {
//            }
//
//            @Override
//            public Optional<Identifiable> findOne(String identifierKey, String id) {
//                // TODO Auto-generated method stub
//                return null;
//            }
//        };
//        //repos.setDbRepository(aRepository);
//        //app.setRepositories(null);
//
//        assertThat(app.getRepositoryIds(),contains("io.skysail.domain.core.test.AThing"));
//        assertThat(app.getRepository("io.skysail.domain.core.test.AThing"), is(aRepository));
//    }

    @Test
    public void toString_contains_main_details() {
        ApplicationModel app = new ApplicationModel("app56");
        assertThat(app.toString(), containsString("app56"));
    }

    @Test
    public void toString_is_formatted_nicely() {
        ApplicationModel app = new ApplicationModel("app37")
                .addOnce(new EntityModel<IdentifiableSupertype>("e23")
                        .add(new FieldModel("f23", String.class)))
                .addOnce(new EntityModel<IdentifiableSupertype>("e24"));

        String[] toString = app.toString().split("\n");

        int i=0;
        assertThat(toString[i++],is("ApplicationModel: app37"));
        assertThat(toString[i++],is("Entities: "));
        assertThat(toString[i++],is(" * EntityModel: id='e23', isAggregate=true"));
        assertThat(toString[i++],is("   Fields:"));
        assertThat(toString[i++],is("    - FieldModel(name=f23, type=String, inputType=null)"));
        assertThat(toString[i++],is(""));
        assertThat(toString[i++],is(" * EntityModel: id='e24', isAggregate=true"));
        assertThat(toString[i++],is(""));
        assertThat(toString[i++],is("Repositories: "));
        assertThat(toString[i++],is("Repositories(repositories={})"));
    }
}
