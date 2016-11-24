package io.skysail.server.app.ref.singleentity.resources.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.data.Form;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skysail.api.responses.EntityServerResponse;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ref.singleentity.Account;
import io.skysail.server.app.ref.singleentity.SingleEntityApplication;
import io.skysail.server.app.ref.singleentity.SingleEntityRepository;
import io.skysail.server.app.ref.singleentity.resources.AccountResource;
import io.skysail.server.app.ref.singleentity.resources.AccountsResource;
import io.skysail.server.app.ref.singleentity.resources.PostAccountResource;
import io.skysail.server.app.ref.singleentity.resources.PutAccountResource;
import io.skysail.server.db.OrientGraphDbService;
import io.skysail.server.testsupport.cucumber.CucumberStepContext;

public class AccountStepDefs extends StepDefs {

    private AccountsResource getListResource;
    private List<Account> accounts;
    private PostAccountResource postResource;
    private PutAccountResource putResource;
    private AccountResource getAccountResource;

    private EntityServerResponse<Account> entity2;

    // === GIVEN ============================================================================

    @Given("^a running AccountApplication$")
    public void a_clean_AccountApplication() {
        super.setUp(new SingleEntityApplication(),new CucumberStepContext(Account.class));

        Repositories repos = new Repositories();
        SingleEntityRepository repo = new SingleEntityRepository();
        OrientGraphDbService dbService = new OrientGraphDbService();
        dbService.activate();
        repo.setDbService(dbService);
        repo.activate();
        repos.setDbRepository(repo);
        ((SingleEntityApplication) application).setRepositories(repos);

        getListResource = setupResource(new AccountsResource());
        getAccountResource = setupResource(new AccountResource());
        postResource = setupResource(new PostAccountResource());
        putResource = setupResource(new PutAccountResource());
    }

    @Given("^(I am logged in as |I log in as )'(.+)'$")
    public void i_am_logged_in_as_admin(String regex, String username) {
        Principal principal = new Principal() {
            @Override
            public String getName() {
                return username;
            }
        };
        Mockito.when(authenticationService.getPrincipal(org.mockito.Matchers.any(Request.class))).thenReturn(principal);
    }


    // === WHENS ========================================================================

    @When("^I add an account like this:$")
    public void postData(Map<String, String> data) {
        stepContext.post(postResource, data);
    }

//    @When("^user '(.+)' adds an account like this:$")
//    public void postDataAsUser(String username, Map<String, String> data) {
//        stepContext.post(postResource, data);
//    }

    @When("^I query all accounts$")
    public void i_query_all_accounts() {
        accounts = getListResource.getEntities(stepContext.getVariant()).getEntity();
    }

    @When("^I open the account page$")
    public void i_open_the_account_page() {
        prepareRequest(getAccountResource);
        entity2 = getAccountResource.getResource(stepContext.getVariant());
    }

    @When("^I change its '(.+)' to '(.+)'$")
    public void i_change_it_s_name_to(String key, String value) {
        prepareRequest(getAccountResource);
        EntityServerResponse<Account> lastEntity = getAccountResource.getResource(stepContext.getVariant());
        Form form = new Form();
        form.add("id", lastEntity.getEntity().getId());
        form.add("name", value);
        form.add("iban", lastEntity.getEntity().getIban());
        prepareRequest(putResource);
        putResource.put(stepContext.formFor(
                "id:" + lastEntity.getEntity().getId(),
                "name:" + value// ,
        // "iban:"+lastEntity.getEntity().getIban()
        ), stepContext.getVariant());
    }

    @When(".*delete it again")
    public void i_delete_it_again() {
        prepareRequest(getAccountResource);
        stepContext.delete(getAccountResource);
        //lastEntity = getAccountResource.deleteEntity(stepContext.getVariant());
    }

    // === THENs ========================================================================

    @Then("^the accounts list page contains such an account:$")
    public void the_result_contains_an_account_with(Map<String, String> data) {
        assertThat(accounts, hasItem(validAccountWith(stepContext.substitute(data), "name", "iban")));
    }

    @Then("^the page contains:$")
    public void the_page_contains(Map<String, String> data) {
        assertThat(accountAsList(entity2), hasItem(validAccountWith(stepContext.substitute(data), "name", "iban")));
    }


    @Then("^I get a '(.+)' response$")
    public void i_get_specific_response(String responseType) {
        assertThat(stepContext.getLastResponse().toString(), containsString(responseType));
    }

    @Then("^the page contains '(.+)'$")
    public void the_page_contains_theString(String name) {
//        List<Account> accounts = getListResource.getEntities(stepContext.getVariant()).getEntity();
//        assertThat(accounts, org.hamcrest.Matchers.hasItem(validAccountWith(substitute(null), "name", "iban")));
        assertThat(entity2.toString(), containsString(name));
    }

    @Then("^the result does not contain an account with name '(.+)'$")
    public void the_result_does_not_contain_an_account_with_name(String name) {
        List<Account> accounts = getListResource.getEntities(stepContext.getVariant()).getEntity();
        //assertThat(accounts, not(org.hamcrest.Matchers.hasItem(validAccountWith(substitute(null), "name", "iban"))));
        Optional<String> found = accounts.stream().map(account -> account.getName()).filter(n -> n.equals(name)).findFirst();
        assertThat(found.isPresent(), is(false));
    }

    @Then("^the page contains a newer created date$")
    public void the_page_contains_a_newer_created_date() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    // === Helper =======================================

    private List<Account> accountAsList(EntityServerResponse<Account> entity22) {
        List<Account> list = new ArrayList<>();
        list.add(entity22.getEntity());
        return list;
    }


}
