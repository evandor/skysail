package io.skysail.server.app.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.resources.swagger.SwaggerSpec;
import io.skysail.server.domain.jvm.SkysailApplicationModel;
import io.skysail.server.restlet.resources.EntityServerResource;

public class SwaggerResource extends EntityServerResource<SwaggerSpec> {


	@Override
	public SkysailResponse<?> eraseEntity() {
		return null;
	}

	@Override
	public SwaggerSpec getEntity() {
		return new SwaggerSpec(getApplication(),getRequest());
	}

}
