package io.skysail.server.restlet.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.restlet.data.Parameter;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.filter.FilterParser;
import io.skysail.server.utils.params.FilterParamUtils;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FacetBuckets {

    private Map<String, AtomicInteger> buckets = new HashMap<>();

    private Map<String, String> names = new HashMap<>();

    private Map<String, String> selected = new HashMap<>();

    private Map<String, String> location = new HashMap<>();

    private String fieldname;

    private String format = "";

    public FacetBuckets(String fieldname, Map<String, AtomicInteger> b, String format) {
        this(fieldname,b, null ,format);
    }

    public FacetBuckets(String fieldname, Map<String, AtomicInteger> b, Map<String,String> names, String format) {
        this.fieldname = fieldname;
        this.buckets = b;
        this.format = format;
        if (names == null) {
            b.keySet().stream().forEach(k -> this.names.put(k, k));
        } else {
            this.names = names;
        }
        b.keySet().stream().forEach(k -> selected.put(k, ""));
    }

    public void setLocation(FieldFacet facet, FilterParser filterParser, FilterParamUtils filterParamUtils) {
    	Parameter filterParameter = filterParamUtils.getFilterParameter();
    	if (filterParameter != null) {
            Set<String> selected = filterParser.getSelected(filterParameter.getValue());
            setSelected(selected);
        }

        buckets.keySet().forEach(key -> {
        	if (selected.containsKey(key) && "checked".equals(selected.get(key))) {
                location.put(key, filterParamUtils.reduceMatchFilter(key, facet, format));
        	} else {
                String setMatchFilter = filterParamUtils.setMatchFilter(key, facet, format);
                location.put(key, setMatchFilter);
        	}
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
