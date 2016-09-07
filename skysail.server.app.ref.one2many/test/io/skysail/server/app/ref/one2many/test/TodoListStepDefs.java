package io.skysail.server.app.ref.one2many.test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.restlet.data.Form;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.skysail.api.responses.ListServerResponse;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.domain.core.Repositories;
import io.skysail.server.app.ref.one2many.One2ManyApplication;
import io.skysail.server.app.ref.one2many.One2ManyRepository;
import io.skysail.server.app.ref.one2many.TodoList;
import io.skysail.server.app.ref.one2many.resources.PostTodoListResource;
import io.skysail.server.app.ref.one2many.resources.TodoListsResource;
import io.skysail.server.db.OrientGraphDbService;
import io.skysail.server.db.validators.UniqueNameValidator;
import io.skysail.server.testsupport.cucumber.CucumberStepContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TodoListStepDefs extends StepDefs {

    private TodoListsResource getListResource;
    private List<TodoList> todolists;
    private PostTodoListResource postResource;
    private SkysailResponse<TodoList> lastResponse;
    // private PutAccountResource putResource;
    // private AccountResource getAccountResource;

    @Given("^a running OneToManyApplication$")
    public void a_clean_AccountApplication() {
        super.setUp(new One2ManyApplication(),new CucumberStepContext(TodoList.class));

        Repositories repos = new Repositories();
        One2ManyRepository repo = new One2ManyRepository();
        OrientGraphDbService dbService = new OrientGraphDbService();
        dbService.activate();
        repo.setDbService(dbService);
        repo.activate();
        repos.setRepository(repo);
        ((One2ManyApplication) application).setRepositories(repos);


        getListResource = setupResource(new TodoListsResource());
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
    //
    // @When("^I add an account without name$")
    // public void i_add_an_account_without_name() {
    // lastResponse = postResource.post(new Form(), HTML_VARIANT);
    // }
    //
    // @When("^I change it's '(.+)' to '(.+)'$")
    // public void i_change_it_s_name_to_another_account_II(String key, String
    // value) {
    // String id = lastResponse.getEntity().getId().toString();
    // requestAttributes.put("id", id.replace("#", ""));
    // getAccountResource.init(context, request, new Response(request));
    // EntityServerResponse<Account> lastEntity =
    // getAccountResource.getEntity2(HTML_VARIANT);
    // Form form = new Form();
    // form.add("id", lastEntity.getEntity().getId());
    // form.add("name", lastEntity.getEntity().getName());
    // form.add("iban", lastEntity.getEntity().getIban());
    // putResource.put(form, HTML_VARIANT);
    // }
    //
    // @When("^I open the account page$")
    // public void i_open_the_account_page() {
    // String id = lastResponse.getEntity().getId().toString();
    // requestAttributes.put("id", id.replace("#", ""));
    // getAccountResource.init(context, request, new Response(request));
    // EntityServerResponse<Account> entity2 =
    // getAccountResource.getEntity2(HTML_VARIANT);
    // }
    //

    // === THENS
    // ========================================================================

    @Then("^the todolists page contains such a todolist:$")
    public void the_result_contains_an_account_with(Map<String, String> data) {
        assertThat(todolists, hasItem(validAccountWith(stepContext.substitute(data), "listname")));
    }

//    @Then("^the result contains a todolist with name '(.+)'$")
//    public void the_result_contains_a_todolist_with_name(String name) {
//        TodoList entity = new TodoList();
//        entity.setListname(name);
//        assertThat(todolists, org.hamcrest.Matchers.hasItem(accountWithIdAnd(name)));// );
//    }
    //
    // @Then("^I get a '(.+)' response$")
    // public void i_get_a_Bad_Request_response(String responseType) {
    // assertThat(lastResponse.toString(), containsString(responseType));
    // }
    //
    // @Then("^the page contains '(.+)'$")
    // public void the_page_contains_theaccount(String someString) {
    // //assertThat()
    // }

    // @Then("^the size of the result is (\\d+)\\.$")
    // public void the_size_of_the_result_is(int size) {
    // assertThat(todolists.size(), is(size));
    // }

}
