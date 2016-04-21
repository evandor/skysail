package io.skysail.server.app.webconsole.services;

import org.osgi.framework.ServiceReference;

import io.skysail.domain.html.Field;
import io.skysail.server.app.webconsole.bundles.BundleDescriptor;

public class ServiceDetails extends ServiceDescriptor {

	@Field
	private BundleDescriptor bundle;

	public ServiceDetails(ServiceReference<?> service) {
		super(service);
		this.bundle = new BundleDescriptor(service.getBundle());
	}

}
