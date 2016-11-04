package io.skysail.server.restlet.filter;

import java.util.Collections;
import java.util.Set;

import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.FilterParser;

public class NoOpFilterParser implements FilterParser {

	@Override
	public Set<String> getSelected(String value) {
		return Collections.emptySet();
	}

    @Override
    public ExprNode parse(String filterstring) {
        return null;
    }

}
