package io.skysail.server.filter;

import java.util.Map;
import java.util.Set;

import io.skysail.server.domain.jvm.FieldFacet;


public interface FilterParser {

	Set<String> getSelected(FieldFacet facet, Map<String, String> lines, String value);

    ExprNode parse(String filterstring);

}
