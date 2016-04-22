package io.skysail.server.app.webconsole.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.osgi.framework.Constants;
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
	private Map<String,Object> properties;

	@Field
	private List<BundleDescriptor> usingBundles;

	public ServiceDetails(ServiceReference<?> service) {
		super(service);
		this.bundle = new BundleDescriptor(service.getBundle());
		this.properties = Arrays.stream(service.getPropertyKeys())
			.filter(key -> !key.equals(Constants.SERVICE_ID) && !key.equals(Constants.OBJECTCLASS))
			.filter(key -> { return service.getProperty(key) != null;})
			.collect(Collectors.toMap(Function.identity(), e -> service.getProperty(e)));
		this.usingBundles = Arrays.stream(service.getUsingBundles())
			.map(bundle -> new BundleDescriptor(bundle))
			.collect(Collectors.toList());
	}

}
