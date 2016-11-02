package io.skysail.server.restlet.resources;

import java.util.HashMap;
import java.util.Map;

import io.skysail.server.domain.jvm.FieldFacet;
import lombok.Getter;

/**
 *
 *
 */
public class Facets {

	@Getter
    private Map<String, FacetBuckets> buckets = new HashMap<>();

    public void add(FieldFacet facet, FacetBuckets facetBuckets) {
        buckets.put(facet.getName(),facetBuckets);
    }



}
