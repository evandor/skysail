package io.skysail.server.restlet.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.skysail.server.domain.jvm.FieldFacet;
import lombok.Getter;

/**
 *
 *
 */
public class Facets {

	@Getter
    private Map<String, Map<String, AtomicInteger>> facetBuckets = new HashMap<>();

    public void add(FieldFacet facet, Map<String, AtomicInteger> buckets) {
        facetBuckets.put(facet.getName(),buckets);
    }



}
