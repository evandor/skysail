package io.skysail.server.app.demo.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.demo.Bookmark;
import io.skysail.server.app.demo.DemoApplication;
import io.skysail.server.app.demo.timetable.timetables.resources.TimetablesResource;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class BookmarksResource extends ListServerResource<Bookmark> {

	private DemoApplication app;

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
	}

	@Override
	public List<Bookmark> getEntity() {
		Filter filter = new Filter(getRequest());
		Pagination pagination = new Pagination(getRequest(), getResponse());
		return app.getRepo().find(filter, pagination);
	}

	@Override
	public List<Link> getLinks() {
		return super.getLinks(PostBookmarkResource.class, BookmarksResource.class, TimetablesResource.class);
	}
}