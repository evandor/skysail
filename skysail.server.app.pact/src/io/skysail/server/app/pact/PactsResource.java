package io.skysail.server.app.pact;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.ResourceContextId;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.queryfilter.pagination.Pagination;
import io.skysail.server.restlet.resources.ListServerResource;

public class PactsResource extends ListServerResource<Pact> {

	private PactApplication app;

	public PactsResource() {
		super(PactResource.class);
		addToContext(ResourceContextId.LINK_TITLE, "list Pacts");
	}

	public PactsResource(Class<? extends PactResource> cls) {
		super(cls);
	}

	@Override
	protected void doInit() {
		app = (PactApplication) getApplication();
	}

	@Override
	public List<Pact> getEntity() {
		Filter filter = new Filter(getRequest());
		Pagination pagination = new Pagination(getRequest(), getResponse());
		return app.getRepo().find(filter, pagination);
	}

	@Override
	public List<Link> getLinks() {
		return super.getLinks(
		        PostPactResource.class,
		        PactsResource.class);
	}
}