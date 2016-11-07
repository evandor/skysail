package io.skysail.server.restlet.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import io.skysail.server.utils.params.FilterParamUtils;
import lombok.Getter;

@Getter
public class FacetBuckets {

    private Map<String, AtomicInteger> buckets = new HashMap<>();

    private String format = ";YYYY";

    private Map<String, String> selected = new HashMap<>();

    private Map<String, String> location = new HashMap<>();

    private String fieldname;

    public FacetBuckets(String fieldname, Map<String, AtomicInteger> b) {
        this.fieldname = fieldname;
        this.buckets = b;
        b.keySet().stream().forEach(k -> selected.put(k, "checked"));
    }

    public void setLocation(Set<String> selectionSet, FilterParamUtils filterParamUtils) {

        buckets.keySet().forEach(key -> {
            String setMatchFilter = filterParamUtils.setMatchFilter(key, format);
            location.put(key, setMatchFilter);
        });
    }

    public void setSelected(Set<String> selectionSet) {
        buckets.keySet().forEach(key -> {
            if (selectionSet.contains(key)) {
                selected.put(key, "checked");
            } else {
                selected.put(key, "");
            }
        });
    }

}
