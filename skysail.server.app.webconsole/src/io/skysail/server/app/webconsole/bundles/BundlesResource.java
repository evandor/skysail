package io.skysail.server.app.webconsole.bundles;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.webconsole.WebconsoleApplication;
import io.skysail.server.app.webconsole.osgi.OsgiService;
import io.skysail.server.app.webconsole.services.ServicesResource;
import io.skysail.server.restlet.resources.ListServerResource;

public class BundlesResource extends ListServerResource<BundleDescriptor> {

	private OsgiService osgiService;

	public BundlesResource() {
		super(BundleResource.class);
		setDescription("returns the OSGi bundles of the framework");
	}
	
	@Override
	protected void doInit() {
		super.doInit();
		osgiService = ((WebconsoleApplication)getApplication()).getOsgiService();
	}
	
	@Override
	public List<BundleDescriptor> getEntity() {
		return osgiService.getBundleDescriptors();
	}
	
	@Override
	public List<Link> getLinks() {
		return super.getLinks(BundlesResource.class, ServicesResource.class);
	}
}
 