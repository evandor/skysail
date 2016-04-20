package io.skysail.server.app.webconsole;

import java.util.List;

import io.skysail.api.links.Link;
import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class BundleResource extends EntityServerResource<BundleDetails> {

	public BundleResource() {
		setDescription("returns the current OSGi bundle's datails");
	}

	@Override
	public SkysailResponse<?> eraseEntity() {
		return new SkysailResponse<>();
	}

	@Override
	public BundleDetails getEntity() {
		return ((WebconsoleApplication)getApplication()).getBundle(getAttribute("id"));
	}
	
	@Override
	public List<Link> getLinks() {
		return super.getLinks(BundleResource.class);
	}

}
