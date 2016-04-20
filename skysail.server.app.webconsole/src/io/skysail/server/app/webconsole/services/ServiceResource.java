package io.skysail.server.app.webconsole.services;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.webconsole.WebconsoleApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

public class ServiceResource extends EntityServerResource<ServiceDetails> {

	public ServiceResource() {
		setDescription("returns the current OSGi service's datails");
	}

	@Override
	public SkysailResponse<?> eraseEntity() {
		return new SkysailResponse<>();
	}

	@Override
	public ServiceDetails getEntity() {
		return ((WebconsoleApplication)getApplication()).getService(getAttribute("id"));
	}
	
	@Override
	public List<Link> getLinks() {
		return super.getLinks(ServiceResource.class);
	}

}
