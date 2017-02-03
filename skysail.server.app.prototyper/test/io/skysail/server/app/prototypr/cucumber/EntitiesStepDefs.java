package io.skysail.server.app.prototypr.cucumber;

import java.util.List;
import java.util.Map;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import io.skysail.api.responses.EntityServerResponse;
import io.skysail.server.app.designer.DesignerApplication;
import io.skysail.server.app.designer.application.DbApplication;
import io.skysail.server.app.designer.entities.DbEntity;
import io.skysail.server.app.designer.entities.resources.EntitiesResource;
import io.skysail.server.app.designer.entities.resources.EntityResource;
import io.skysail.server.app.designer.entities.resources.PostEntityResource;
import io.skysail.server.app.designer.entities.resources.PutEntityResource;
import io.skysail.server.app.designer.repo.DesignerRepository;
import io.skysail.server.db.OrientGraphDbService;
import io.skysail.server.db.validators.UniqueNameForParentValidator;
import io.skysail.server.db.validators.UniqueNameValidator;

public class EntitiesStepDefs extends StepDefs {

    private EntitiesResource getListResource;
    private List<DbEntity> entities;
    private PostEntityResource postResource;
    private PutEntityResource putResource;
    private EntityResource getEntityResource;

    private EntityServerResponse<DbApplication> entity2;

    private Scenario scenario;

    public EntitiesStepDefs(AutomationApi api) {
        super(api);
        super.setUp(new DesignerApplication(), new CucumberStepContext(DbApplication.class));

//        Repositories repos = new Repositories();
        DesignerRepository repo = new DesignerRepository();
        OrientGraphDbService dbService = new OrientGraphDbService();
        dbService.activate();
        repo.setDbService(dbService);
        repo.activate();
//        repos.setRepository(repo);
//        ((DesignerApplication) application).setRepositories(repos);

        getListResource = setupResource(new EntitiesResource());
        getEntityResource = setupResource(new EntityResource());
        postResource = setupResource(new PostEntityResource());
        putResource = setupResource(new PutEntityResource());

        new UniqueNameValidator().setDbService(dbService);
        new UniqueNameForParentValidator().setDbService(dbService);
    }

    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
    }

    // === GIVEN =========================================================================

    @Given("^an application like this:$")
    public void an_application_like_this(Map<String, String> data) {
        ApplicationsStepDefs appStepDefs = (ApplicationsStepDefs) this.api.getStepDef(ApplicationsStepDefs.class);
        appStepDefs.postData(data);
    }

    // === WHENS ===============================================================

    @When("^I add an entity like this:$")
    public void i_add_an_entity_like_this(Map<String, String> data) {
        prepareRequest(postResource);
        stepContext.post(postResource, data);
    }

    // === THENS
    // ========================================================================

    // === Helper =======================================

}
