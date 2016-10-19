package io.skysail.server.app.pline.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.pline.Bookmark;
import io.skysail.server.app.pline.TemplateApplication;
import io.skysail.server.app.pline.TemplateRepository;
import io.skysail.server.queryfilter.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class BookmarksResource extends ListServerResource<Bookmark> {

	private TemplateApplication app;
	private TemplateRepository repository;

	public BookmarksResource() {
		super(BookmarkResource.class);
		addToContext(ResourceContextId.LINK_TITLE, "list Bookmarks");
	}

	public BookmarksResource(Class<? extends BookmarkResource> cls) {
		super(cls);
	}

	@Override
	protected void doInit() {
		app = (TemplateApplication) getApplication();
		repository = (TemplateRepository) app.getRepository(Bookmark.class);
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