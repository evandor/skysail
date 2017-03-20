package io.skysail.app.notes;

import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.ConfigurationPolicy
import org.osgi.service.component.annotations.Reference
import org.restlet.data.Protocol
import io.skysail.core.app.SkysailApplication
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy
import io.skysail.core.app.ApplicationProvider
import io.skysail.server.menus.MenuItemProvider
import io.skysail.core.app.ApiVersion
import org.osgi.service.component.annotations.Activate
import org.osgi.service.component.ComponentContext
import io.skysail.core.app.ApplicationConfiguration
import org.restlet.data.Protocol
import io.skysail.server.restlet.RouteBuilder
import io.skysail.server.menus.MenuItem
import java.util.Arrays
import io.skysail.app.notes.resources.NotesResource
import io.skysail.server.db.DbService
import io.skysail.app.notes.repository.NotesRepository
import org.osgi.service.component.annotations.ReferenceCardinality
import io.skysail.app.notes.resources.PutNoteResource
import io.skysail.app.notes.resources.NoteResource
import io.skysail.app.notes.resources.PostNoteResource

object NotesApplication {
  final val APP_NAME = "notes"
}

@Component(
  immediate = true,
  configurationPolicy = ConfigurationPolicy.OPTIONAL,
  service = Array(classOf[ApplicationProvider], classOf[MenuItemProvider]))
class NotesApplication extends SkysailApplication(
  NotesApplication.APP_NAME,
  new ApiVersion(int2Integer(1))) with MenuItemProvider {

  setDescription("notes app")
  getConnectorService().getClientProtocols().add(Protocol.HTTPS)

  @Reference(cardinality = ReferenceCardinality.MANDATORY)
  var dbService: DbService = null

  @Activate
  override def activate(appConfig: ApplicationConfiguration, componentContext: ComponentContext) = {
    super.activate(appConfig, componentContext);
    addRepository(new NotesRepository(dbService));
  }

  override def attach() = {
    router.attach(new RouteBuilder("", classOf[NotesResource]));
    router.attach(new RouteBuilder("/notes", classOf[NotesResource]));
    router.attach(new RouteBuilder("/notes/", classOf[PostNoteResource]));
    router.attach(new RouteBuilder("/notes/{id}", classOf[NoteResource]));
    router.attach(new RouteBuilder("/notes/{id}/", classOf[PutNoteResource]));
    createStaticDirectory();
  }

}