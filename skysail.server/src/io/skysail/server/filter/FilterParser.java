package io.skysail.server.filter;

import java.util.Set;


public interface FilterParser {

	Set<String> getSelected(String value);

    ExprNode parse(String filterstring);

}
