package io.skysail.server.app.reference.singleentity.resources.test;

import org.restlet.Response;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.reference.singleentity.SingleEntityApplication;
import io.skysail.server.app.reference.singleentity.SingleEntityRepository;
import io.skysail.server.app.reference.singleentity.resources.AccountsResource;
import io.skysail.server.db.OrientGraphDbService;

public class AccountStepDefs extends StepDefs {

    private AccountsResource resource;

    @Given("^a clean AccountApplication$")
    public void a_clean_AccountApplication() {
        super.setUp(new SingleEntityApplication());

        resource = new AccountsResource();
        resource.setRequest(request);

        Repositories repos = new Repositories();
        SingleEntityRepository repo = new SingleEntityRepository();
        OrientGraphDbService dbService = new OrientGraphDbService();
        dbService.activate();
        repo.setDbService(dbService);
        repo.activate();
        repos.setRepository(repo);
        ((SingleEntityApplication) application).setRepositories(repos);

        resource.init(context, request, new Response(request));
    }

    @When("^I query all accounts$")
    public void i_query_all_accounts() {
        throw new PendingException();
    }

    @Then("^the size of the result is (\\d+)\\.$")
    public void the_size_of_the_result_is(int size) {
        throw new PendingException();
    }

}
