package io.skysail.server.app.routes.resources;

import java.util.List;

import io.skysail.server.ResourceContextId;
import io.skysail.server.app.routes.RouteDescription;
import io.skysail.server.app.routes.RoutesApplication;
import io.skysail.server.restlet.resources.ListServerResource;

public class RoutesResource extends ListServerResource<RouteDescription> {

	private RoutesApplication app;

	public RoutesResource() {
		addToContext(ResourceContextId.LINK_TITLE, "list Bookmarks");
	}

	@Override
	protected void doInit() {
		app = (RoutesApplication) getApplication();
	}

	@Override
	public List<RouteDescription> getEntity() {
	    return app.getRoutesList();
	}
}