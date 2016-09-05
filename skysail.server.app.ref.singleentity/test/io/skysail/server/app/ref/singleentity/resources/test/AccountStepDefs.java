package io.skysail.server.app.ref.singleentity.resources.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.restlet.Response;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.engine.resource.VariantInfo;

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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccountStepDefs extends StepDefs {

    private static final String RANDOM_IDENT = "<random>";

    private static final VariantInfo VARIANT = new VariantInfo(MediaType.TEXT_HTML);

    private AccountsResource getListResource;
    private List<Account> accounts;
    private PostAccountResource postResource;
    private PutAccountResource putResource;
    private AccountResource getAccountResource;

    private Map<String, String> randoms = new HashMap<>();

    private EntityServerResponse<Account> entity2;

    public static Matcher<Account> validAccountWith(Map<String, String> data, String... keys) {
        return new TypeSafeMatcher<Account>() {

            @Override
            public void describeTo(Description desc) {
                desc.appendText("expected result: account with non-null id, creationDate");
                Arrays.stream(keys).forEach(key -> {
                    desc.appendText(", " + key + " = ")
                        .appendValue(data.get(key));
                });

            }

            @Override
            protected boolean matchesSafely(Account account) {
                if (account.getCreated() == null) {
                    return false;
                }
                if (account.getId() == null) {
                    return false;
                }
                if (data.get("iban") != null) {
                    if (!data.get("iban").equals(account.getIban())) {
                        return false;
                    }
                }
                if (!account.getName().equals(data.get("name"))) {
                    return false;
                }
                return true;
            }
        };
    }

    // === GIVEN ============================================================================

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

    // === WHENS ========================================================================

    @When("^I add an account like this:$")
    public void i_add_an_account_like_this(Map<String, String> data) {
        Form form = new Form();
        data.keySet().stream().forEach(key -> handleKey(data, form, key));
        log.info("posting form {}",form);
        lastResponse = postResource.post(form, VARIANT);
    }

    @When("^I query all accounts$")
    public void i_query_all_accounts() {
        accounts = getListResource.getEntities(VARIANT).getEntity();
    }

    @When("^I change its '(.+)' to '(.+)'$")
    public void i_change_it_s_name_to(String key, String value) {
        prepareRequest(getAccountResource);
        EntityServerResponse<Account> lastEntity = getAccountResource.getResource(VARIANT);
        Form form = new Form();
        form.add("id", lastEntity.getEntity().getId());
        form.add("name", value);
        form.add("iban", lastEntity.getEntity().getIban());
        prepareRequest(putResource);
        putResource.put(formFor(
                "id:" + lastEntity.getEntity().getId(),
                "name:" + value// ,
        // "iban:"+lastEntity.getEntity().getIban()
        ), VARIANT);
    }

    @When("^I open the account page$")
    public void i_open_the_account_page() {
        prepareRequest(getAccountResource);
        entity2 = getAccountResource.getResource(VARIANT);
    }

    @When("^I delete it again$")
    public void i_delete_it_again() {
        prepareRequest(getAccountResource);
        EntityServerResponse<Account> deletedEntity = getAccountResource.deleteEntity(VARIANT);
        System.out.println(deletedEntity);
    }

    // === THENS ========================================================================

    @Then("^the accounts list page contains such an account:$")
    public void the_result_contains_an_account_with(Map<String, String> data) {
        assertThat(accounts, hasItem(validAccountWith(substitute(data), "name", "iban")));
    }

    @Then("^the page contains:$")
    public void the_page_contains(Map<String, String> data) {
        assertThat(accountAsList(entity2), hasItem(validAccountWith(substitute(data), "name", "iban")));
    }


    private List<Account> accountAsList(EntityServerResponse<Account> entity22) {
        List<Account> list = new ArrayList<>();
        list.add(entity22.getEntity());
        return list;
    }

    @Then("^I get a '(.+)' response$")
    public void i_get_specific_response(String responseType) {
        assertThat(lastResponse.toString(), containsString(responseType));
    }

    @Then("^the page contains '(.+)'$")
    public void the_page_contains_theaccount(String name) {
        List<Account> accounts = getListResource.getEntities(VARIANT).getEntity();
        assertThat(accounts, org.hamcrest.Matchers.hasItem(validAccountWith(substitute(null), "name", "iban")));
    }

    @Then("^the result does not contain an account with name '(.+)'$")
    public void the_result_does_not_contain_an_account_with_name(String name) {
        List<Account> accounts = getListResource.getEntities(VARIANT).getEntity();
        assertThat(accounts, not(org.hamcrest.Matchers.hasItem(validAccountWith(substitute(null), "name", "iban"))));
    }

    @Then("^the page contains a newer created date$")
    public void the_page_contains_a_newer_created_date() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    // === HELPERS ====================================================================

    private boolean handleKey(Map<String, String> data, Form form, String key) {
        String value = data.get(key);
        if (value.contains(RANDOM_IDENT)) {
            String randomString = new BigInteger(130, new SecureRandom()).toString(32);
            randoms.put(key, randomString);
            value = value.replace(RANDOM_IDENT, randomString);
        }
        return form.add(key, value);
    }

    private Form formFor(String... str) {
        Form form = new Form();
        Arrays.stream(str).forEach(input -> {
            String[] split = input.split(":", 2);
            form.add(split[0], split[1]);
        });
        return form;
    }

    private Map<String, String> substitute(Map<String, String> data) {
        Map<String, String> result = new HashMap<>();
        data.entrySet().stream().forEach(e -> substitute(result, e));
        return result;
    }

    private void substitute(Map<String,String> result, Entry<String, String> entry) {
        String value = entry.getValue();
        if (value.contains(RANDOM_IDENT)) {
            value = value.replace(RANDOM_IDENT, randoms.get(entry.getKey()));
        }
        result.put(entry.getKey(), value);
    }


}
