package io.skysail.server.app.mxgraph.poc.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.mxgraph.poc.Bookmark;
import io.skysail.server.app.mxgraph.poc.MxGraphPocApplication;
import io.skysail.server.app.mxgraph.poc.MxGraphPocRepository;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class BookmarksResource extends ListServerResource<Bookmark> {

	private MxGraphPocApplication app;
	private MxGraphPocRepository repository;

	public BookmarksResource() {
		super(BookmarkResource.class);
		addToContext(ResourceContextId.LINK_TITLE, "list Bookmarks");
	}

	public BookmarksResource(Class<? extends BookmarkResource> cls) {
		super(cls);
	}

	@Override
	protected void doInit() {
		app = (MxGraphPocApplication) getApplication();
		repository = (MxGraphPocRepository) app.getRepository(Bookmark.class);
	}

	@Override
	public List<Bookmark> getEntity() {
		Filter filter = new Filter(getRequest());
		Pagination pagination = new Pagination(getRequest(), getResponse());
		return repository.find(filter, pagination);
	}

	@Override
	public List<Link> getLinks() {
		return super.getLinks(PostBookmarkResource.class, BookmarksResource.class);
	}
}