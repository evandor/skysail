package io.skysail.server.app.webconsole.services;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.app.webconsole.WebconsoleApplication;
import io.skysail.server.app.webconsole.bundles.BundlesResource;
import io.skysail.server.app.webconsole.osgi.OsgiService;
import io.skysail.server.restlet.resources.EntityServerResource;

public class ServiceResource extends EntityServerResource<ServiceDetails> {

	private OsgiService osgiService;

	public ServiceResource() {
		setDescription("returns the current OSGi service's datails");
	}
	
	@Override
	protected void doInit() {
		super.doInit();
		osgiService = ((WebconsoleApplication)getApplication()).getOsgiService();
	}

	@Override
	public SkysailResponse<?> eraseEntity() {
		return new SkysailResponse<>();
	}

	@Override
	public ServiceDetails getEntity() {
		return osgiService.getService(getAttribute("id"));
	}
	
	@Override
	public List<Link> getLinks() {
		return super.getLinks(BundlesResource.class, ServicesResource.class, ServiceResource.class);
	}

}
