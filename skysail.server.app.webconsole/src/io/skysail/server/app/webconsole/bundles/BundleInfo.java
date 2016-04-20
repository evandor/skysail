package io.skysail.server.app.webconsole.bundles;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BundleInfo {

	private final String name;
	private final Object value;
	private final BundleInfoType type;
	private final String description;

}
