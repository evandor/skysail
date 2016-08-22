package io.skysail.server.app.ref.singleentity.resources.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.engine.resource.VariantInfo;

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

public class AccountStepDefs extends StepDefs {

    private AccountsResource getListResource;
    private List<Account> accounts;
    private PostAccountResource postResource;
    private PutAccountResource putResource;
    private AccountResource getAccountResource;
    private VariantInfo variant;

    public static Matcher<Account> accountWithIdCreationDateAnd(String name) {
        return new TypeSafeMatcher<Account>() {

            @Override
            public void describeTo(Description desc) {
                desc.appendText("expected result: account with non-null id, creationDate and name of '")
                        .appendValue(name)
                        .appendText("'");
            }

            @Override
            protected boolean matchesSafely(Account account) {
                if (account.getCreated() == null) {
                    return false;
                }
                return account.getId() == null ? false : name.equals(account.getName());
            }
        };
    }

    @Given("^a running AccountApplication$")
    public void a_clean_AccountApplication() {
        super.setUp(new SingleEntityApplication());

        getListResource = new AccountsResource();
        getListResource.setRequest(request);

        getAccountResource = new AccountResource();
        getAccountResource.setRequest(request);

        postResource = new PostAccountResource();
        postResource.setRequest(request);

        putResource = new PutAccountResource();
        putResource.setRequest(request);

        Repositories repos = new Repositories();
        SingleEntityRepository repo = new SingleEntityRepository();
        OrientGraphDbService dbService = new OrientGraphDbService();
        dbService.activate();
        repo.setDbService(dbService);
        repo.activate();
        repos.setRepository(repo);
        ((SingleEntityApplication) application).setRepositories(repos);

        getListResource.init(context, request, new Response(request));
        postResource.init(context, request, new Response(request));
        getAccountResource.init(context, request, new Response(request));
        putResource.init(context, request, new Response(request));
    }

    @Given("^I am using a browser$")
    public void i_am_using_a_browser() throws Throwable {
        variant = new VariantInfo(MediaType.TEXT_HTML);
    }


    @When("^I query all accounts$")
    public void i_query_all_accounts() {
        accounts = getListResource.getEntities(variant).getEntity();
    }

    @When("^I add an account with name '(.+)'$")
    public void i_add_an_account_with_name_theaccount(String name) {
        Form form = new Form();
        form.add("name", name);
        lastResponse = postResource.post(form, variant);
    }

    @When("^I add an account without name$")
    public void i_add_an_account_without_name() {
        lastResponse = postResource.post(new Form(), variant);
    }

    @When("^I change its '(.+)' to '(.+)'$")
    public void i_change_it_s_name_to(String key, String value) {
        prepareRequest(getAccountResource);
        EntityServerResponse<Account> lastEntity = getAccountResource.getEntity2(variant);
        Form form = new Form();
        form.add("id", lastEntity.getEntity().getId());
        form.add("name", value);
        form.add("iban", lastEntity.getEntity().getIban());
        prepareRequest(putResource);
        putResource.put(form, variant);
    }

    @When("^I open the account page$")
    public void i_open_the_account_page() {
    	prepareRequest(getAccountResource);
        getAccountResource.getEntity2(variant);
    }

    @When("^I delete it again$")
    public void i_delete_it_again() {
    	prepareRequest(getAccountResource);
        EntityServerResponse<Account> deletedEntity = getAccountResource.deleteEntity(variant);
        System.out.println(deletedEntity);
    }

    @Then("^the result contains an account with name '(.+)'$")
    public void the_result_contains_an_account_with_name(String name) {
        Account account = new Account();
        account.setName(name);
        assertThat(accounts, org.hamcrest.Matchers.hasItem(accountWithIdCreationDateAnd(name)));// );
    }

    @Then("^I get a '(.+)' response$")
    public void i_get_a_Bad_Request_response(String responseType) {
        assertThat(lastResponse.toString(), containsString(responseType));
    }

    @Then("^the page contains '(.+)'$")
    public void the_page_contains_theaccount(String name) {
        List<Account> accounts = getListResource.getEntities(variant).getEntity();
        assertThat(accounts, org.hamcrest.Matchers.hasItem(accountWithIdCreationDateAnd(name)));
    }

    @Then("^the result does not contain an account with name '(.+)'$")
    public void the_result_does_not_contain_an_account_with_name_account_beDeleted(String name) {
       List<Account> accounts = getListResource.getEntities(variant).getEntity();
       assertThat(accounts, not(org.hamcrest.Matchers.hasItem(accountWithIdCreationDateAnd(name))));
    }

}
