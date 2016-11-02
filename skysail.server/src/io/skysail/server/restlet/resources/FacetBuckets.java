package io.skysail.server.restlet.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

@Getter
public class FacetBuckets {

    private Map<String, AtomicInteger> buckets = new HashMap<>();

    private String format = ";YYYY";

    private Map<String, String> selected = new HashMap<>();

    public FacetBuckets(Map<String, AtomicInteger> b) {
        this.buckets = b;
        b.keySet().stream().forEach(k -> selected.put(k, "checked"));
    }

}
