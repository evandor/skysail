package io.skysail.server.app.webconsole.osgi;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import io.skysail.server.app.webconsole.bundles.BundleDescriptor;
import lombok.extern.slf4j.Slf4j;

@Component(service = OsgiService.class)
@Slf4j
public class OsgiService {

	private BundleContext bundleContext;

	@Activate
	public void activate(ComponentContext ctx) {
		bundleContext = ctx.getBundleContext();
	}

	@Activate
	public void deactivate(ComponentContext ctx) {
		bundleContext = null;
	}
	
	public List<BundleDescriptor> getBundleDescriptors() {
		if (bundleContext == null) {
			log.warn("bundleContext not available");
			return Collections.emptyList();
		}
		return Arrays.stream(bundleContext.getBundles()) // NOSONAR
				.map(b -> new BundleDescriptor(b))
				.collect(Collectors.toList());
	}
}
