package io.skysail.domain.core.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.EntityModel;
import io.skysail.domain.core.FieldModel;

public class EntitiesStepDefs {
	
	private ApplicationModel applicationModel;
	private String name;
	private String currentEntityName;

	@Given("^An Application with name '(.+)'$")
	public void an_Application_with_name_appName(String appName) {
		applicationModel = new ApplicationModel(appName);
	}

	@When("^I query the applications name$")
	public void i_query_the_applications_name() {
		name = applicationModel.getName();
	}
	
	@When("I add an entity called '(.+)'")
	public void i_add_an_entity_called(String entityName) {
		applicationModel.addOnce(new EntityModel<>(entityName));
		this.currentEntityName = entityName;
	}

	@Then("^I'll get '(.+)'$")
	public void i_ll_get_notesApplication(String appName) {
		assertThat(name,is(appName));
	}
	
	@Then("the list of entities of that application has size (\\d+)")
	public void the_list_of_entities_has_size(int size) {
		assertThat(applicationModel.getEntityIds().size(),is(size));
		assertThat(applicationModel.getEntityValues().size(),is(size));
	}

	@Then("the list of fields of that entity has size (\\d+)")
	public void the_list_of_fields_has_size(int size) {
		EntityModel<?> entity = applicationModel.getEntity(currentEntityName);
		assertThat(entity.getFieldNames().size(),is(size));
		assertThat(entity.getFields().size(),is(size));
	}

	@When("^I add a field called '(.+)' of type '(.+)' to that entity$")
	public void add_Field_To_Entity(String fieldName, String fieldType) throws ClassNotFoundException {
		EntityModel<?> entity = applicationModel.getEntity(currentEntityName);
		entity.add(new FieldModel(fieldName, Class.forName(fieldType)));
	}

	@Then("^I validate the outcomes$")
	public void i_validate_the_outcomes() {
	}
}
