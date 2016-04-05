package io.skysail.server.app.demo;

import java.util.Arrays;
import java.util.List;

import io.skysail.server.restlet.resources.ListServerResource;

public class UnprotectedArrayResource extends ListServerResource<Time> {

	@Override
	public List<?> getEntity() {
		return Arrays.asList(new Time(), new Time());
	}

}
