package io.skysail.server.app.notes.scala.resources

import io.skysail.domain.Identifiable
import io.skysail.server.restlet.resources.ListServerResource
import io.skysail.server.app.notes.scala.domain.Note
import io.skysail.server.queryfilter.Filter
import io.skysail.server.queryfilter.pagination.Pagination
import io.skysail.server.app.notes.scala.ScalaNotesApplication
import io.skysail.server.app.notes.scala.repo.NotesRepository

class BookmarksResource extends ListServerResource[Note] {

  val app: ScalaNotesApplication;
  val repository: NotesRepository;
  /*
	public BookmarksResource() {
		super(BookmarkResource.class);
		addToContext(ResourceContextId.LINK_TITLE, "list Bookmarks");
	}

	public BookmarksResource(Class<? extends BookmarkResource> cls) {
		super(cls);
	}

	@Override
	protected void doInit() {
		app = (DemoApplication) getApplication();
		repository = (DemoRepository) app.getRepository(Bookmark.class);
	}*/

  override def getEntity(): java.util.List[Note] = {
    val filter = new Filter(getRequest());
    val pagination = new Pagination(getRequest(), getResponse(), repository.count(filter));
    repository.find(filter, pagination);
  }

  /*@Override
	public List<Link> getLinks() {
		return super.getLinks(PostBookmarkResource.class, BookmarksResource.class, RamlClientResource.class, TimetablesResource.class);
	} */
}