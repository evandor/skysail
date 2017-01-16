package io.skysail.domain.core.fields.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.skysail.domain.AutomationApi;
import io.skysail.domain.core.EntityModel;
import io.skysail.domain.core.FieldModel;
import io.skysail.domain.html.InputType;
import io.skysail.domain.test.StepDefs;

public class FieldStepDefs extends StepDefs {

    private FieldModel fieldModel;

    // private AutomationApi automationApi;

    private Map<String, FieldModel> fieldModels = new HashMap<>();

    public FieldStepDefs(AutomationApi automationApi) {
        // this.automationApi = automationApi;
        System.out.println(automationApi.getName());
    }

    // --- given -----------------------------------------

    @Given("^A Field with name '(.+)' of type '([^,]+)'$")
    public void a_Field_with_name(String fieldName, String fieldType) throws ClassNotFoundException {
        fieldModel = new FieldModel(new EntityModel("test"), "title", Class.forName(fieldType));
    }

    @Given("^A Field with name '(.+)' of type '(.+)', referred to as '(.+)'$")
    public void a_Field_with_namereference(String fieldName, String fieldType, String modelName)
            throws ClassNotFoundException {
        fieldModel = new FieldModel(new EntityModel("test"), "title", Class.forName(fieldType));
        fieldModels.put(modelName, fieldModel);
    }

    @Given("^the inputType is set to '(.+)'$")
    public void the_inputType_is_set_to(String inputType) {
        fieldModel.setInputType(InputType.valueOf(InputType.class, inputType));
    }

    // --- when ------------------------------------------

    // --- then ------------------------------------------

    @Then("^the fields '(.+)' attribute value is '(.*)'$")
    public void the_fields_property_is(String propertyName, String value) throws Exception {
        assertThat(propertyValue(fieldModel, propertyName).toString(), is(value));
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
