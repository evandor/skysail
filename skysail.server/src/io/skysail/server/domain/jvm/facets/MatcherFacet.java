package io.skysail.server.domain.jvm.facets;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.facets.FacetType;
import io.skysail.server.restlet.resources.FacetBuckets;
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
    public FacetBuckets bucketsFrom(Field field, List<?> list) {
        Map<String, AtomicInteger> b = new HashMap<>();
        b.put(value, new AtomicInteger(0));
        list.stream()
                .forEach(t -> {
                    try {
                        // Object object = field.get(t);
                        b.get(value).incrementAndGet();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });
        return new FacetBuckets(b);
    }

    @Override
    public String sqlFilterExpression(String value) {
        this.value = value;
        return new StringBuilder(getName()).append("=:").append(getName()).toString();
    }

}
