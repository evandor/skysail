package io.skysail.server.app.webconsole;

import java.util.List;

import io.skysail.server.restlet.resources.ListServerResource;

public class BundlesResource extends ListServerResource<BundleDescriptor> {

	@Override
	public List<BundleDescriptor> getEntity() {
		return ((WebconsoleApplication)getApplication()).getBundles();
	}

}
