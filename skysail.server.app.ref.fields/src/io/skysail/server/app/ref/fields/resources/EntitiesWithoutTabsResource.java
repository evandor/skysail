package io.skysail.server.app.ref.fields.resources;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.ref.fields.EntityWithoutTabs;
import io.skysail.server.app.ref.fields.FieldsDemoApplication;
import io.skysail.server.entities.GenerateResources;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

@GenerateResources
public class EntitiesWithoutTabsResource extends ListServerResource<EntityWithoutTabs> {

	private FieldsDemoApplication app;

	public EntitiesWithoutTabsResource() {
		super(EntityWithoutTabResource.class);
		addToContext(ResourceContextId.LINK_TITLE, "list entities w/o tabs");
	}

//	public EntitiesWithoutTabsResource(Class<? extends BookmarkResource> cls) {
//		super(cls);
//	}

	@Override
	protected void doInit() {
		app = (FieldsDemoApplication) getApplication();
	}

	@Override
	public List<EntityWithoutTabs> getEntity() {
		Filter filter = new Filter(getRequest());
		Pagination pagination = new Pagination(getRequest(), getResponse());
		return app.getEntitiesWoTabsRepo().find(filter, pagination);
	}

	@Override
	public List<Link> getLinks() {
		return super.getLinks(PostEntityWithoutTabsResource.class, BookmarksResource.class, EntitiesWithoutTabsResource.class);
	}
}