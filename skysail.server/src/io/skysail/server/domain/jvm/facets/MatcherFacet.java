package io.skysail.server.domain.jvm.facets;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.facets.FacetType;
import lombok.extern.slf4j.Slf4j;

/**
 * A {@link FieldFacet} defining buckets of related data by filtering by the value of a field.
 *
 * Configuration example:
 *
 * i.am.a.package.Transaction.category.TYPE = MATCH
 *
 * @see FacetType
 */
@Slf4j
public class MatcherFacet extends FieldFacet {

    private String value;

    public MatcherFacet(String id, Map<String, String> config) {
        super(id, config);
    }

    @Override
    public Map<String, AtomicInteger> bucketsFrom(Field field, List<?> list) {
        Map<String, AtomicInteger> buckets = new HashMap<>();
        buckets.put(value, new AtomicInteger(0));
        list.stream()
                .forEach(t -> {
                    try {
                        // Object object = field.get(t);
                        buckets.get(value).incrementAndGet();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });
        return buckets;
    }

    @Override
    public String sqlFilterExpression(String value) {
        this.value = value;
        return new StringBuilder(getName()).append("=:").append(getName()).toString();
    }

}