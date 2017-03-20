package io.skysail.app.notes.repository

import io.skysail.domain.core.repos.DbRepository
import io.skysail.server.db.GraphDbRepository
import io.skysail.server.db.DbService
import io.skysail.app.notes.domain.Note
import io.skysail.server.db.DbClassName

class NotesRepository(db: DbService) extends GraphDbRepository[Note] with DbRepository {
  this.dbService = db
  dbService.createWithSuperClass("V", DbClassName.of(classOf[Note]));
  dbService.register(classOf[Note]);
}