package io.skysail.server.app.reference.singleentity.resources.test;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class AccountStepDefs {

	@Given("^a clean AccountApplication$")
	public void a_clean_AccountApplication() throws Throwable {
		//throw new PendingException();
	}
	
	@When("^I query all accounts$")
	public void i_query_all_accounts() throws Throwable {
	    throw new PendingException();
	}

	@Then("^the size of the result is (\\d+)\\.$")
	public void the_size_of_the_result_is(int size) throws Throwable {
	    throw new PendingException();
	}


	
}
