package io.skysail.domain.core.entities.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.skysail.domain.Entity;
import io.skysail.domain.core.EntityModel;
import lombok.Data;

public class EntityStepDefs {

	@Data
	public class Dummy implements Entity {
		private String id;
	}

	private String name;
	private EntityModel<Dummy> currentEntityModel;
	private EntityModel<Dummy> anothterEntityModel;

	@Given("^An Entity with name '(.+)'$")
	public void an_Entity_with_name_note(String entityName) {
		currentEntityModel = new EntityModel<>(entityName);
	}

	@Given("^Another Entity with name '(.+)'$")
	public void another_Entity_with_name(String name) {
		anothterEntityModel = new EntityModel<>(name);
	}

	@Then("^the two entities are equal\\.$")
	public void the_two_entities_are_equal() {
		assertThat(currentEntityModel.equals(anothterEntityModel),is(true));
	}

	@Then("^I'll get '(.+)'$")
	public void i_ll_get_notesApplication(String appName) {
		assertThat(name, is(appName));
	}


	@Then("^I validate the outcomes$")
	public void i_validate_the_outcomes() {
	}
}
