package io.skysail.server.app.notes.scala.repo

import io.skysail.server.db.GraphDbRepository
import io.skysail.server.app.notes.scala.domain.Note
import io.skysail.domain.core.repos.DbRepository
import org.osgi.service.component.annotations.Component
import org.osgi.service.component.annotations.Reference
import io.skysail.server.db.DbService
import org.osgi.service.component.annotations.Activate
import io.skysail.server.db.DbClassName

@Component(immediate = true)//, property = "name=NoteRepository")
class NotesRepository extends GraphDbRepository[Note] with DbRepository {

    @Reference
    def setDbService(dbService: DbService) {
        this.dbService = dbService;
    }

    def unsetDbService(dbService: DbService) {
        this.dbService = null;
    }

    @Activate
    def activate() {
        dbService.createWithSuperClass("V", DbClassName.of(classOf[Note]));
        dbService.register(classOf[Note]);
    }

}