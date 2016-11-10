package io.skysail.server.filter;

import java.util.Set;

import io.skysail.server.domain.jvm.FieldFacet;


public interface FilterParser {

	Set<String> getSelected(FieldFacet facet, String value);

    ExprNode parse(String filterstring);

}
