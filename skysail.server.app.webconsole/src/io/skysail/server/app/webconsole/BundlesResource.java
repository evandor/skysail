package io.skysail.server.app.webconsole;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.webconsole.services.ServicesResource;
import io.skysail.server.restlet.resources.ListServerResource;

public class BundlesResource extends ListServerResource<BundleDescriptor> {

	public BundlesResource() {
		super(BundleResource.class);
		setDescription("returns the OSGi bundles of the framework");
	}
	
	@Override
	public List<BundleDescriptor> getEntity() {
		return ((WebconsoleApplication)getApplication()).getBundles();
	}
	
	@Override
	public List<Link> getLinks() {
		return super.getLinks(BundlesResource.class, ServicesResource.class);
	}

}
