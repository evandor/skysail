package io.skysail.app.notes.resources

import io.skysail.server.restlet.resources.ListServerResource
import io.skysail.app.notes.domain.Note
import java.util.Collections
import io.skysail.app.notes.PostNoteResource

class NotesResource extends ListServerResource[Note] {
  def getEntity(): java.util.List[Note] =  Collections.emptyList()
  override def getLinks() = super.getLinks(classOf[PostNoteResource])
}