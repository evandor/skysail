package io.skysail.server.domain.jvm.facets;

import java.lang.reflect.Field;
import java.util.Date;
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
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * A {@link FieldFacet} defining buckets of related data by filtering the year of a date field.
 *
 * Configuration example:
 *
 * i.am.a.package.Transaction.buchungstag.TYPE = YEAR
 * i.am.a.package.Transaction.buchungstag.START = 2000
 *
 * This will define a range of "buckets", one for each year from 2000 until now (default for "END_IDENT").
 *
 * @see FacetType
 */
@Slf4j
@ToString(callSuper = true)
public class YearFacet extends FieldFacet {

    private static final String START_IDENT = "START";
    private static final String END_IDENT = "END";

    @Getter
    private Integer start;

    @Getter
    private Integer end;

    public YearFacet(@NonNull String id, Map<String,String> config) {
        super(id);
        start = getBorder(START_IDENT, config); // default "now"
        end = getBorder(END_IDENT, config);     // default "now"
    }

    @Override
    public FacetBuckets bucketsFrom(Field field, List<?> list) {
        Map<String, AtomicInteger> buckets = new HashMap<>();
        list.stream()
            .forEach(t -> {
                try {
                    Date date = (Date) field.get(t);
                    @SuppressWarnings("deprecation")
                    int year = (1900 + date.getYear());
                    if (!buckets.containsKey(String.valueOf(year))) {
                        buckets.put(String.valueOf(year), new AtomicInteger());
                    }
                    buckets.get(String.valueOf(year)).incrementAndGet();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            });
        return new FacetBuckets(field.getName(),buckets,"YYYY");
    }

    @Override
    public String sqlFilterExpression(String value, String operatorSign) {
        return new StringBuilder(getName()).append(".format('YYYY')").append(operatorSign).append(getName()).toString();
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

    private Integer getBorder(String key, Map<String, String> config) {
        String theValue = config.get(key);
        if (theValue == null || "".equals(theValue.trim()) || "NOW".equals(theValue)) {
            return new Date().getYear() + 1900;
        } else {
            return Integer.parseInt(theValue);
        }
    }



}
