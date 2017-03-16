package io.skysail.server.app.demo;

import org.apache.commons.lang.NotImplementedException;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class UnprotectedTimeResource extends EntityServerResource<Time> {

	@Override
	public SkysailResponse<Time> eraseEntity() {
		throw new NotImplementedException();
	}

	@Override
	public Time getEntity() {
		return new Time();
	}

}
