package io.skysail.server.app.webconsole.services;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.osgi.framework.ServiceReference;

import io.skysail.domain.html.Field;
import io.skysail.server.app.webconsole.bundles.BundleDescriptor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiceDetails extends ServiceDescriptor {

	@Field
	private BundleDescriptor bundle;

	@Field
	private String properties;

	@Field
	private List<BundleDescriptor> usingBundles;

	public ServiceDetails(ServiceReference<?> service) {
		super(service);
		this.bundle = new BundleDescriptor(service.getBundle());
		this.properties = Arrays.stream(service.getPropertyKeys())
			.filter(key -> { return service.getProperty(key) != null;})
			.map(key -> {return key + " -> " + service.getProperty(key);})
			.collect(Collectors.joining(", "));
		this.usingBundles = Arrays.stream(service.getUsingBundles())
			.map(bundle -> new BundleDescriptor(bundle))
			.collect(Collectors.toList());
	}

}
