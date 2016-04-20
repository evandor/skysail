package io.skysail.server.app.webconsole.utils;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BundleContextUtil {

	public static final String FWK_PROP_WORK_CONTEXT = "felix.webconsole.work.context";
	public static final String WORK_CTX_OWN = "own";
	public static final String WORK_CTX_SYSTEM = "system";
	
	public static BundleContext getWorkingBundleContext(final BundleContext bc) {
		if (WORK_CTX_SYSTEM.equalsIgnoreCase(bc.getProperty(FWK_PROP_WORK_CONTEXT))) {
			return bc.getBundle(Constants.SYSTEM_BUNDLE_LOCATION).getBundleContext();
		}
		return bc;
	}
}