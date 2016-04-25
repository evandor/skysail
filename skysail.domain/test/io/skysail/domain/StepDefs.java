package io.skysail.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skysail.domain.core.ApplicationModel;

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

	@Then("^I'll get 'notesApplication'$")
	public void i_ll_get_notesApplication() {
		assertThat(name,is("notesApplication"));
	}
}
