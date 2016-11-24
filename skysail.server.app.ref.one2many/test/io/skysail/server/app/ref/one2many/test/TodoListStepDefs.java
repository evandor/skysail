package io.skysail.server.app.ref.one2many.test;

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
import io.skysail.server.app.ref.one2many.One2ManyApplication;
import io.skysail.server.app.ref.one2many.One2ManyRepository;
import io.skysail.server.app.ref.one2many.TodoList;
import io.skysail.server.app.ref.one2many.resources.PostTodoListResource;
import io.skysail.server.app.ref.one2many.resources.TodoListResource;
import io.skysail.server.app.ref.one2many.resources.TodoListsResource;
import io.skysail.server.db.OrientGraphDbService;
import io.skysail.server.db.validators.UniqueNameValidator;
import io.skysail.server.testsupport.cucumber.CucumberStepContext;

public class TodoListStepDefs extends StepDefs {

    private TodoListsResource getListResource;
    private List<TodoList> todolists;
    private PostTodoListResource postResource;
    private SkysailResponse<TodoList> lastResponse;
    private TodoListResource getTodoListResource;
    // private PutAccountResource putResource;
    // private AccountResource getAccountResource;
    private EntityServerResponse<TodoList> entity2;

    @Given("^a running OneToManyApplication$")
    public void a_clean_AccountApplication() {
        super.setUp(new One2ManyApplication(), new CucumberStepContext(TodoList.class));

//        Repositories repos = new Repositories();
        OrientGraphDbService dbService = new OrientGraphDbService();
        One2ManyRepository repo = new One2ManyRepository(dbService);
        dbService.activate();
        repo.setDbService(dbService);
        repo.activate();
//        repos.setDbRepository(repo);
//        ((One2ManyApplication) application).setRepositories(repos);

        getListResource = setupResource(new TodoListsResource());
        getTodoListResource = setupResource(new TodoListResource());
        postResource = setupResource(new PostTodoListResource());

        new UniqueNameValidator().setDbService(dbService);
    }

    // === WHENS
    // ========================================================================

    @When("^I add a todolist like this:$")
    public void post(Map<String, String> data) {
        stepContext.post(postResource, data);
    }

    @When("^I query all todolists$")
    public void i_query_all_todolists() {
        ListServerResponse<TodoList> entities = getListResource.getEntities(stepContext.getVariant());
        todolists = entities.getEntity();
    }

    @When("^I add a todolist with name '(.+)'$")
    public void i_add_a_todolist_with_name(String name) {
        Form form = new Form();
        form.add("listname", name);
        lastResponse = postResource.post(form, stepContext.getVariant());
    }

    @When("^I open the todolist page$")
    public void i_open_the_todolist_page() {
        prepareRequest(getTodoListResource);
        entity2 = getTodoListResource.getResource(stepContext.getVariant());
    }

    // === THENS ===============================================================

    @Then("^the todolists page contains such a todolist:$")
    public void the_result_contains_an_account_with(Map<String, String> data) {
        assertThat(todolists, hasItem(validAccountWith(stepContext.substitute(data), "listname")));
    }

    @Then("^I get a '(.+)' response$")
    public void i_get_specific_response(String responseType) {
        assertThat(stepContext.getLastResponse().toString(), containsString(responseType));
    }

    @Then("^the todolist page contains '(.+)'$")
    public void the_page_contains_theString(String name) {
        assertThat(entity2.toString(), containsString(name));
    }

}
