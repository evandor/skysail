package io.skysail.server.facets;

import io.skysail.server.domain.jvm.FieldFacet;

public class NoOpFacetsProvider extends FacetsProvider {

	public FieldFacet getFacetFor(String string) {
		return null;
	}
}
