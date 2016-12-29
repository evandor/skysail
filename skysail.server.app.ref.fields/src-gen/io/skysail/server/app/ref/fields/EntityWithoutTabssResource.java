package io.skysail.server.app.ref.fields;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.ref.fields.EntityWithoutTabs;
import io.skysail.server.app.ref.fields.FieldsDemoApplication;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;


public class EntityWithoutTabssResource extends ListServerResource<EntityWithoutTabs> {

	private FieldsDemoApplication app;

	public EntityWithoutTabssResource() {
		super(EntityWithoutTabsResource.class);
		addToContext(ResourceContextId.LINK_TITLE, "list entities w/o tabs");
	}

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
		return super.getLinks(PostEntityWithoutTabsResource.class);//, BookmarksResource.class, EntitiesWithoutTabsResource.class);
	}

}
