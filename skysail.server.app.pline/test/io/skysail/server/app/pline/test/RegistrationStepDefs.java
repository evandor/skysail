package io.skysail.server.app.pline.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.restlet.data.Form;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skysail.api.responses.EntityServerResponse;
import io.skysail.api.responses.ListServerResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.pline.PlineApplication;
import io.skysail.server.app.pline.PlineRepository;
import io.skysail.server.app.pline.Registration;
import io.skysail.server.app.pline.resources.PostRegistrationResource;
import io.skysail.server.app.pline.resources.RegistrationResource;
import io.skysail.server.app.pline.resources.RegistrationsResource;
import io.skysail.server.db.OrientGraphDbService;
import io.skysail.server.testsupport.cucumber.CucumberStepContext;

public class RegistrationStepDefs extends StepDefs {

    private RegistrationsResource getListResource;
    private List<Registration> registrations;
    private PostRegistrationResource postResource;
    private SkysailResponse<Registration> lastResponse;
    private RegistrationResource getRegistrationResource;
    // private PutAccountResource putResource;
    // private AccountResource getAccountResource;
    private EntityServerResponse<Registration> entity2;

    @Given("^a running PlineApplication$")
    public void a_clean_AccountApplication() {
        super.setUp(new PlineApplication(), new CucumberStepContext(Registration.class));

//        Repositories repos = new Repositories();
        OrientGraphDbService dbService = new OrientGraphDbService();
        dbService.activate();
        PlineRepository repo = new PlineRepository(dbService);
        repo.setDbService(dbService);
        repo.activate();
  //      repos.setDbRepository(repo);
    //    ((PlineApplication) application).setRepositories(repos);

        getListResource = setupResource(new RegistrationsResource());
        getRegistrationResource = setupResource(new RegistrationResource());
        postResource = setupResource(new PostRegistrationResource());

        //new UniqueNameValidator().setDbService(dbService);
    }

    // === WHENS
    // ========================================================================

    @When("^I add a registration like this:$")
    public void post(Map<String, String> data) {
        stepContext.post(postResource, data);
    }

    @When("^I query all registrations$")
    public void i_query_all_registrations() {
        ListServerResponse<Registration> entities = getListResource.getEntities(stepContext.getVariant());
        registrations = entities.getEntity();
    }

    @When("^I add a registration with name '(.+)'$")
    public void i_add_a_registration_with_name(String name) {
        Form form = new Form();
        form.add("listname", name);
        lastResponse = postResource.post(form, stepContext.getVariant());
    }

    @When("^I open the registration page$")
    public void i_open_the_registration_page() {
        prepareRequest(getRegistrationResource);
        entity2 = getRegistrationResource.getResource(stepContext.getVariant());
    }

    // === THENS ===============================================================

    @Then("^the registrations page contains such a registration:$")
    public void the_result_contains_an_account_with(Map<String, String> data) {
        assertThat(registrations, hasItem(validAccountWith(stepContext.substitute(data), "listname")));
    }

    @Then("^I get a '(.+)' response$")
    public void i_get_specific_response(String responseType) {
        assertThat(stepContext.getLastResponse().toString(), containsString(responseType));
    }

    @Then("^the registration page contains '(.+)'$")
    public void the_page_contains_theString(String name) {
        assertThat(entity2.toString(), containsString(name));
    }

}
