package io.skysail.domain.core.applications.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skysail.domain.Entity;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.EntityModel;
import io.skysail.domain.core.FieldModel;
import lombok.Data;

public class ApplicationStepDefs {

	@Data
	public class Dummy implements Entity {
		private String id;
	}

	private String currentEntityName;
	private ApplicationModel applicationModel;

	@Given("^An Application with name '(.+)'$")
	public void an_Application_with_name_appName(String appName) {
		applicationModel = new ApplicationModel(appName);
	}

	@Then("the list of entities of that application (still )?has size (\\d+)")
	public void the_list_of_entities_has_size(Object dummy, int size) {
		assertThat(applicationModel.getEntityIds().size(), is(size));
		assertThat(applicationModel.getEntityValues().size(), is(size));
	}
//
//	@Then("the list of fields of that entity has size (\\d+)")
//	public void the_list_of_fields_has_size(int size) {
//		assertThat(currentEntityModel.getFieldNames().size(), is(size));
//		assertThat(currentEntityModel.getFields().size(), is(size));
//	}

	@When("^I query the applications name$")
	public void i_query_the_applications_name() {
		//currentAppName = applicationModel.getName();
	}

	@When("^I add a field called '(.+)' of type '(.+)' to that entity$")
	public void add_Field_To_Entity(String fieldName, String fieldType) throws ClassNotFoundException {
		EntityModel<? extends Entity> entity = applicationModel.getEntity(currentEntityName);
		entity.add(new FieldModel(entity, fieldName, Class.forName(fieldType)));
	}

	@When("^I add a field called '(.+)' of type '(.+)' to that other entity$")
	public void add_Field_To_OtherEntity(String fieldName, String fieldType) throws ClassNotFoundException {
		EntityModel<?> entity = applicationModel.getEntity(currentEntityName);
		entity.add(new FieldModel(entity, fieldName, Class.forName(fieldType)));
	}


}
