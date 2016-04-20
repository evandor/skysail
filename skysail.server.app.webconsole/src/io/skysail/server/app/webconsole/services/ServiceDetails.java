package io.skysail.server.app.webconsole.services;

import org.osgi.framework.ServiceReference;

public class ServiceDetails extends ServiceDescriptor {

	public ServiceDetails(ServiceReference<?> service) {
		super(service);
	}

}
