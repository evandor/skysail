package io.skysail.server.domain.jvm.facets;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.skysail.server.domain.jvm.FieldFacet;

public class MatcherFacet extends FieldFacet {

    public MatcherFacet(String id, Map<String, String> config) {
        super(id, config);
    }

    @Override
    public Map<Integer, AtomicInteger> bucketsFrom(Field field, List<?> list) {
        return Collections.emptyMap();
    }

    @Override
    public String sqlFilterExpression(String value) {
        return new StringBuilder(getName()).append("=:").append(getName()).toString();
    }

}
