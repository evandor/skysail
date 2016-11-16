package io.skysail.server.domain.jvm.facets;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.restlet.data.Form;
import org.restlet.data.Parameter;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.facets.FacetType;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.restlet.resources.FacetBuckets;
import io.skysail.server.utils.params.FilterParamUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * A {@link FieldFacet} defining buckets of related data by filtering by the value of a field.
 *
 * Configuration example:
 *
 * i.am.a.package.Transaction.category.TYPE = MATCH
 *
 * This will create _one_ bucket, containing all the elements
 *
 * @see FacetType
 */
@Slf4j
public class MatcherFacet extends FieldFacet {

    private String value;

    public MatcherFacet(@NonNull String id, Map<String, String> config) {
        super(id);
    }

    @Override
    public FacetBuckets bucketsFrom(Field field, List<?> list) {
        Map<String, AtomicInteger> b = new HashMap<>();
        b.put(value, new AtomicInteger(0));
        list.stream()
                .forEach(t -> {
                    try {
                        b.get(value).incrementAndGet();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });
        return new FacetBuckets(field.getName(),b,"");
    }

    @Override
    public String sqlFilterExpression(String value, String operatorSign) {
        this.value = value;
        return new StringBuilder(getName()).append(operatorSign).append(getName()).toString();
    }

    @Override
    public Form addFormParameters(Form newForm, String fieldname, String format, String value) {
        newForm.add(new Parameter(FilterParamUtils.FILTER_PARAM_KEY, "(" + fieldname + format + "=" + value + ")"));
        return newForm;
    }

    @Override
    public boolean match(ExprNode node, Object gotten, String value) {
        return false;
    }

}
