package io.skysail.server.app.dashboard;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.EntityServerResource;

public class DashboardResource extends EntityServerResource<Nothing> {

	public DashboardResource() {
		addToContext(ResourceContextId.RENDERER_HINT, "mdb");
	}
	
	@Override
	public SkysailResponse<?> eraseEntity() {
		return null;
	}

	@Override
	public Nothing getEntity() {
		return new Nothing();
	}

}