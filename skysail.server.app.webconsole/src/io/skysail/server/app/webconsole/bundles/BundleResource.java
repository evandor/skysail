package io.skysail.server.app.webconsole.bundles;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.server.app.webconsole.WebconsoleApplication;
import io.skysail.server.app.webconsole.osgi.OsgiService;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.webconsole.osgi.entities.bundles.BundleDetails;

public class BundleResource extends EntityServerResource<BundleDetails> {

	private OsgiService osgiService;

	public BundleResource() {
		setDescription("returns the current OSGi bundle's datails");
	}

	@Override
	protected void doInit() {
		super.doInit();
		osgiService = ((WebconsoleApplication)getApplication()).getOsgiService();
	}

	@Override
	public BundleDetails getEntity() {
		return osgiService.getBundleDetails(getAttribute("id"));
	}

	@Override
	public List<Link> getLinks() {
		return super.getLinks(BundleResource.class);
	}

}
