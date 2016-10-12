package io.skysail.server.app.metrics.resources;

import java.util.List;
import java.util.stream.Collectors;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.app.metrics.MetricsApplication;
import io.skysail.server.restlet.resources.ListServerResource;

public class TimersResource extends ListServerResource<Timer> {

	private MetricsApplication app;

	public TimersResource() {
		super(BookmarkResource.class);
		addToContext(ResourceContextId.LINK_TITLE, "list Bookmarks");
	}

	public TimersResource(Class<? extends BookmarkResource> cls) {
		super(cls);
	}

	@Override
	protected void doInit() {
		app = (MetricsApplication) getApplication();
	}

	@Override
	public List<Timer> getEntity() {
		return app.getTimerMetrics().stream().map(t -> new Timer(t)).collect(Collectors.toList());
	}

	@Override
	public List<Link> getLinks() {
		return super.getLinks(PostBookmarkResource.class, TimersResource.class);
	}
}