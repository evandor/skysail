package io.skysail.app.notes

import io.skysail.server.restlet.resources.PostEntityServerResource
import io.skysail.app.notes.domain.Note

class PostNoteResource extends PostEntityServerResource[Note] {
  var app = getApplication().asInstanceOf[NotesApplication];
  def createEntityTemplate(): Note = new Note()
  def addEntity(entity: Note): Unit = app.getRepository(classOf[Note]).save(entity, getApplicationModel())
}