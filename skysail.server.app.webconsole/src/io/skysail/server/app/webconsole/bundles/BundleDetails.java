package io.skysail.server.app.webconsole.bundles;

import java.util.Dictionary;

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;

import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class BundleDetails extends BundleDescriptor {

	@Field
	private String location;

	@Field
	private long lastModification;

	@Field
	private String docUrl;

	@Field
	private String vendor;

	@Field
	private String copyright;

	@Field
	private String description;

	@Field
	private Integer startLevel;

	@Field
	private String bundleClasspath;

	public BundleDetails(Bundle bundle) {
		super(bundle);
		Dictionary<?, ?> headers = bundle.getHeaders(null);
		this.location = bundle.getLocation();
		this.lastModification = bundle.getLastModified();
		this.docUrl = (String) headers.get(Constants.BUNDLE_DOCURL);
		this.vendor = (String) headers.get(Constants.BUNDLE_VENDOR);
		this.copyright = (String) headers.get(Constants.BUNDLE_COPYRIGHT);
		this.description = (String) headers.get(Constants.BUNDLE_DESCRIPTION);
		this.bundleClasspath = (String) headers.get(Constants.BUNDLE_CLASSPATH);
		this.startLevel = getStartLevel(bundle);
	}

	private Integer getStartLevel(Bundle bundle) {
		if (bundle.getState() == Bundle.UNINSTALLED) {
			return null;
		}
//		StartLevel sl = getStartLevel();
//		if (sl != null) {
//			return new Integer(sl.getBundleStartLevel(bundle));
		//}
		return 0;
	}

}
