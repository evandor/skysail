package io.skysail.server.restlet.filter;

import java.util.Collections;
import java.util.Set;

public class NoOpFilterParser implements FilterParser {

	@Override
	public Set<String> getSelected(String value) {
		return Collections.emptySet();
	}

}
