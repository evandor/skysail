package io.skysail.server.app.webconsole;

import org.osgi.framework.Bundle;

import io.skysail.domain.Identifiable;
import io.skysail.domain.html.Field;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BundleDescriptor implements Identifiable {

	private String id;
	
	@Field
	private String symbolicName;
	
	public BundleDescriptor(Bundle bundle) {
		id = Long.toString(bundle.getBundleId());
		symbolicName = bundle.getSymbolicName();
	}

}
