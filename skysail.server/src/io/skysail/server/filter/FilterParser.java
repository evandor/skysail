package io.skysail.server.restlet.filter;

import java.util.Set;

public interface FilterParser {

	Set<String> getSelected(String value);

}
