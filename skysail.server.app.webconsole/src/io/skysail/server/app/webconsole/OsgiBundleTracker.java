package io.skysail.server.app.webconsole;

import java.util.Arrays;
import java.util.function.Supplier;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import io.skysail.server.app.webconsole.bundles.BundleDetails;

public class OsgiBundleTracker {

	private BundleContext bundleContext;
	private ServiceTracker bundleInfoTracker;
	private String[] bootPkgs;
	private boolean[] bootPkgWildcards;

	public OsgiBundleTracker(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

//	public List<BundleDescriptor> getBundleDescriptors() {
//		Bundle[] bundles = bundleContext.getBundles();
//		return Arrays.stream(bundles).map(b -> new BundleDescriptor(b)).collect(Collectors.toList());
//	}

	public BundleDetails getBundleDetails(String id) {
		Bundle[] bundles = bundleContext.getBundles();
		return Arrays.stream(bundles).filter(b -> id.equals(Long.toString(b.getBundleId()))).findFirst().map(b -> new BundleDetails(b))
				.orElseThrow(new Supplier<IllegalArgumentException>() {
					@Override
					public IllegalArgumentException get() {
						return new IllegalArgumentException("yeah");
					}
				});
	}

	public void deactivate() {
		bundleInfoTracker.close();
		bundleInfoTracker = null;
	}

}
