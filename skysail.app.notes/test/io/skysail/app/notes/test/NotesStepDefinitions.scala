package io.skysail.app.notes.test

import cucumber.api.java.en.Given
import io.skysail.server.db.OrientGraphDbService
import io.skysail.app.notes.NotesApplication
import io.skysail.app.notes.domain.Note
import io.skysail.server.testsupport.cucumber.CucumberStepContext
import io.skysail.app.notes.resources.NotesResource
import io.skysail.core.resources.SkysailServerResource
import io.skysail.app.notes.resources.NoteResource
import io.skysail.app.notes.resources.PutNoteResource
import io.skysail.app.notes.resources.PostNoteResource
import io.skysail.server.Constants
import java.security.Principal
import org.restlet.Response
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.assertThat
import org.mockito.Mockito
import org.restlet.Request
import org.mockito.Matchers.any;
import cucumber.api.java.en.When
import collection.JavaConversions._
import cucumber.api.java.en.Then

class NotesStepDefinitions extends StepDefinitions {

  var notesResource: NotesResource = null
  var noteResource: NoteResource = null
  var postResource: PostNoteResource = null
  var putResource: PutNoteResource = null
  
  var notes: java.util.List[Note] = java.util.Collections.emptyList()

  // === GIVEN ========================================================================

  @Given("^a running NotesApplication$")
  def a_running_NoteApplication(): Unit = {
    val dbService = new OrientGraphDbService();
    dbService.activate();
    val app = new NotesApplication();
    setDbService(dbService, app);
    super.setUp(app, new CucumberStepContext(classOf[Note]));

    notesResource = setupResource(new NotesResource());
    noteResource = setupResource(new NoteResource());
    postResource = setupResource(new PostNoteResource());
    putResource = setupResource(new PutNoteResource());
  }
  
  @Given("^I am logged in as '(.+)'$")
  def i_am_logged_in_as_admin(username:String):Unit = {
    Mockito.when(authService.getPrincipal(any(classOf[Request]))).thenReturn(new Principal() { override def getName() = username })
  }
   
  // === WHENS ========================================================================

  @When("^I add a note like this:$")
  def postData(data: java.util.Map[String, String]):Unit = {
    stepContext.post(postResource, addEntityClassIdentifier(data.toMap));
  }
  
  @When("^I query all notes")
  def i_query_all_notes() = notes = notesResource.getEntities(stepContext.getVariant()).getEntity()

  // === THENs ========================================================================

  @Then("^the notes list page contains such a note:$")
  def the_result_contains_an_account_with(data: java.util.Map[String, String]) {
    assertThat(notes, hasItem(StepDefinitions.validNoteWith(stepContext.substitute(addEntityClassIdentifier(data.toMap)), "content")));
  }
  
  @Then("^I get a 'Created \\((\\d+)\\)' response$")
  def i_get_a_Created_response(statusCode: String) {
    assertThat(stepContext.getLastResponse().toString(), containsString(statusCode));
  }
  
   // === Privates ========================================================================
  
  private def setDbService(dbService: OrientGraphDbService, app: NotesApplication) = {
    try {
      val field = classOf[NotesApplication].getDeclaredField("dbService");
      field.setAccessible(true);
      field.set(app, dbService);
    } catch {
      case e: Throwable => println(e)
    }
  }

  private def setupResource[T <: SkysailServerResource[_]](resource: T): T = {
    resource.setRequest(request);
    resource.init(context, request, new Response(request));
    return resource;
  }
  
  private def addEntityClassIdentifier(data: Map[String, String]) = {
    data.map { case (key,value) => classOf[Note].getName() + Constants.CLASS_FIELD_NAMES_SEPARATOR + key -> value}
    
       // data.entrySet().stream().collect(Collectors.<Map.Entry, String, String> toMap(
       //         e -> Account.class.getName() + Constants.CLASS_FIELD_NAMES_SEPARATOR + e.getKey(), e -> e.getValue().toString()));
  }
}