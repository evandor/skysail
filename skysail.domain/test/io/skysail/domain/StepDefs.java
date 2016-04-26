package io.skysail.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skysail.domain.core.ApplicationModel;
import io.skysail.domain.core.EntityModel;

public class StepDefs {

	private ApplicationModel applicationModel;
	private String name;

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
}
