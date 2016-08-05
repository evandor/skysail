package io.skysail.server.app.reference.singleentity.resources.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.restlet.Response;
import org.restlet.data.Form;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.reference.singleentity.Account;
import io.skysail.server.app.reference.singleentity.SingleEntityApplication;
import io.skysail.server.app.reference.singleentity.SingleEntityRepository;
import io.skysail.server.app.reference.singleentity.resources.AccountsResource;
import io.skysail.server.app.reference.singleentity.resources.PostAccountResource;
import io.skysail.server.db.OrientGraphDbService;

public class AccountStepDefs extends StepDefs {

    private AccountsResource resource;
    private List<Account> accounts;
    private PostAccountResource postResource;

    @Given("^a clean AccountApplication$")
    public void a_clean_AccountApplication() {
        super.setUp(new SingleEntityApplication());

        resource = new AccountsResource();
        resource.setRequest(request);

        postResource = new PostAccountResource();
        postResource.setRequest(request);

        Repositories repos = new Repositories();
        SingleEntityRepository repo = new SingleEntityRepository();
        OrientGraphDbService dbService = new OrientGraphDbService();
        dbService.activate();
        repo.setDbService(dbService);
        repo.activate();
        repos.setRepository(repo);
        ((SingleEntityApplication) application).setRepositories(repos);

        resource.init(context, request, new Response(request));
        postResource.init(context, request, new Response(request));
    }

    @When("^I query all accounts$")
    public void i_query_all_accounts() {
        accounts = resource.getEntity();
    }

    @When("^I add an account with name '(.+)'$")
    public void i_add_an_account_with_name_theaccount(String name) {
        Account entity = new Account();
        entity.setName(name);
        //postResource.addEntity(entity);
        Form Form = new Form();
        //postResource.post(Form, Variant.)
    }

    @When("^I add an account without name$")
    public void i_add_an_account_without_name() {
        Account entity = new Account();
        postResource.addEntity(entity);
    }


    @Then("^the size of the result is (\\d+)\\.$")
    public void the_size_of_the_result_is(int size) {
        assertThat(accounts.size(), is(size));
    }

}
