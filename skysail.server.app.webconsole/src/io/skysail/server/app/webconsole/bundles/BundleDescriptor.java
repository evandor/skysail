package io.skysail.server.app.webconsole.bundles;

import org.osgi.framework.Bundle;

import io.skysail.domain.Nameable;
import io.skysail.domain.html.Field;
import io.skysail.domain.html.InputType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BundleDescriptor implements Nameable {

	@Field
	private String id;
	
	@Field(inputType = InputType.TEXT)
	private String symbolicName;

	@Field
	private String version;
	
	@Field
	private String state;
	
	public BundleDescriptor(Bundle bundle) {
		id = Long.toString(bundle.getBundleId());
		symbolicName = bundle.getSymbolicName();
		version = bundle.getVersion().toString();
		state = translate(bundle.getState());
	}

	private String translate(int bundleState) { // NOSONAR
		switch (bundleState) {
		case Bundle.ACTIVE:
			return "ACTIVE";
		case Bundle.INSTALLED:
			return "INSTALLED";
		case Bundle.RESOLVED:
			return "RESOLVED";
		case Bundle.STARTING:
			return "STARTING";
		case Bundle.STOPPING:
			return "STOPPING";
		case Bundle.UNINSTALLED:
			return "UNINSTALLED";
		default:
			return "unknown";
		}
	}

	@Override
	public String getName() {
		return symbolicName + " ("+version+") [#" + id +"]";
	}

}
