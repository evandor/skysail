package io.skysail.server.facets;

import io.skysail.server.domain.jvm.FieldFacet;

public interface FacetsProvider {

	FieldFacet getFacetFor(String string);

}
