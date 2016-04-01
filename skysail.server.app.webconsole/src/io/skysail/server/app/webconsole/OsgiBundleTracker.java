package io.skysail.server.app.webconsole;

import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.util.tracker.ServiceTracker;

public class OsgiBundleTracker {

	private BundleContext bundleContext;
	private ServiceTracker bundleInfoTracker;
	private String[] bootPkgs;
	private boolean[] bootPkgWildcards;

	public OsgiBundleTracker(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

//		bundleInfoTracker = new ServiceTracker(bundleContext, BundleInfoProvider.class.getName(), null);
//		bundleInfoTracker.open();
//
//		String bootDelegation = bundleContext.getProperty(Constants.FRAMEWORK_BOOTDELEGATION);
//		bootDelegation = (bootDelegation == null) ? "java.*" : bootDelegation + ",java.*";
//		StringTokenizer st = new StringTokenizer(bootDelegation, " ,");
//		bootPkgs = new String[st.countTokens()];
//		bootPkgWildcards = new boolean[bootPkgs.length];
//		for (int i = 0; i < bootPkgs.length; i++) {
//			bootDelegation = st.nextToken();
//			if (bootDelegation.endsWith("*")) {
//				bootPkgWildcards[i] = true;
//				bootDelegation = bootDelegation.substring(0, bootDelegation.length() - 1);
//			}
//			bootPkgs[i] = bootDelegation;
//		}
	}

	public List<BundleDescriptor> getBundles() {
		Bundle[] allBundles = bundleContext.getBundles();
		Bundle[] bundles;
		Bundle bundle = null;
		if (bundle != null) {
			bundles = new Bundle[] { bundle };
		} else {
			bundles = allBundles;
		}

		// Util.sort(bundles, locale);
		return Arrays.stream(bundles).map(b -> new BundleDescriptor(b)).collect(Collectors.toList());
	}

	public void deactivate() {
		bundleInfoTracker.close();
		bundleInfoTracker = null;
	}

}
