package io.skysail.server.app.reference.singleentity.resources.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.representation.Variant;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skysail.api.responses.EntityServerResponse;
import io.skysail.api.responses.ListServerResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.reference.singleentity.Account;
import io.skysail.server.app.reference.singleentity.SingleEntityApplication;
import io.skysail.server.app.reference.singleentity.SingleEntityRepository;
import io.skysail.server.app.reference.singleentity.resources.AccountResource;
import io.skysail.server.app.reference.singleentity.resources.AccountsResource;
import io.skysail.server.app.reference.singleentity.resources.PostAccountResource;
import io.skysail.server.app.reference.singleentity.resources.PutAccountResource;
import io.skysail.server.db.OrientGraphDbService;

public class AccountStepDefs extends StepDefs {

    private static final Variant HTML_VARIANT = new VariantInfo(MediaType.TEXT_HTML);

    private AccountsResource getListResource;
    private List<Account> accounts;
    private PostAccountResource postResource;
    private SkysailResponse<Account> lastResponse;
    private PutAccountResource putResource;
    private AccountResource getAccountResource;

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

    @Before
    public void setUp() {
        System.out.println("Hier");
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

    @When("^I query all accounts$")
    public void i_query_all_accounts() {
        ListServerResponse<Account> entities = getListResource.getEntities(HTML_VARIANT);
        accounts = entities.getEntity();
    }

    @When("^I add an account with name '(.+)'$")
    public void i_add_an_account_with_name_theaccount(String name) {
        Form form = new Form();
        form.add("name", name);
        lastResponse = postResource.post(form, HTML_VARIANT);
    }

    @When("^I add an account without name$")
    public void i_add_an_account_without_name() {
        lastResponse = postResource.post(new Form(), HTML_VARIANT);
    }

    @When("^I change it's '(.+)' to '(.+)'$")
    public void i_change_it_s_name_to_another_account_II(String key, String value) {
        String id = lastResponse.getEntity().getId().toString();
        requestAttributes.put("id", id.replace("#", ""));
        getAccountResource.init(context, request, new Response(request));
        EntityServerResponse<Account> lastEntity = getAccountResource.getEntity2(HTML_VARIANT);
        Form form = new Form();
        form.add("id", lastEntity.getEntity().getId());
        form.add("name", lastEntity.getEntity().getName());
        form.add("iban", lastEntity.getEntity().getIban());
        putResource.put(form, HTML_VARIANT);
    }

    @When("^I open the account page$")
    public void i_open_the_account_page() {
        String id = lastResponse.getEntity().getId().toString();
        requestAttributes.put("id", id.replace("#", ""));
        getAccountResource.init(context, request, new Response(request));
        EntityServerResponse<Account> entity2 = getAccountResource.getEntity2(HTML_VARIANT);
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
    public void the_page_contains_theaccount(String someString) {
        //assertThat()
    }


    // @Then("^the size of the result is (\\d+)\\.$")
    // public void the_size_of_the_result_is(int size) {
    // assertThat(accounts.size(), is(size));
    // }

}
