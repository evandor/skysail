package io.skysail.domain.core.fields.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.skysail.domain.AutomationApi;
import io.skysail.domain.core.FieldModel;

public class FieldStepDefs {

	private FieldModel fieldModel;

	private AutomationApi automationApi;
	
	public FieldStepDefs(AutomationApi automationApi) {
		this.automationApi = automationApi;
		System.out.println(automationApi.getName());
	}

	@Given("^A Field with name '(.+)' of type '(.+)'$")
	public void a_Field_with_name(String fieldName, String fieldType) throws ClassNotFoundException {
		fieldModel = new FieldModel("title", Class.forName(fieldType));
	}

	@Then("^the fields id is '(.+)'$")
	public void the_fields_id_ise(String fieldId) {
		assertThat(fieldModel.getId(), is(fieldId));
	}

	@Then("^the fields name is '(.+)'$")
	public void the_fields_name_is(String fieldName) {
		assertThat(fieldModel.getName(), is(fieldName));
	}

	@Then("^the fields type is '(.+)'$")
	public void the_fields_class_is_java_lang_String(String type) {
		assertThat(fieldModel.getType().toGenericString(), is(type));
	}

	@Then("^the string representation of that field is '(.+)'$")
	public void the_string_representation_of_that_field_is(String toString) {
		assertThat(fieldModel.toString(), is(toString));
	}
	
	@Then("^something happens which has not been implemented$")
	public void something_happens_which_has_not_been_implemented() {
	    throw new PendingException();
	}

}
