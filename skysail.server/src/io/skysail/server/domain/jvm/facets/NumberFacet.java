package io.skysail.server.domain.jvm.facets;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
 * A {@link FieldFacet} defining buckets of related data by filtering by the
 * amount of a number field.
 *
 * Configuration example (see also parent class):
 *
 * i.am.a.package.Transaction.amount.TYPE = NUMBER
 * i.am.a.package.Transaction.amount.BORDERS = -100,100
 *
 * This will define three "buckets", one for the transactions smaller than -100,
 * one for the transactions between -100 and 100 and one for the ones bigger
 * than 100.
 *
 * @see FacetType
 */
@Slf4j
@ToString
public class NumberFacet extends FieldFacet {

    private static final String BORDERS = "BORDERS";

    @Getter
    private List<Double> thresholds = new ArrayList<>(); // e.g [-100.0,100.0]

    public NumberFacet(@NonNull String id, Map<String, String> config) {
        super(id);
        if (config.get(BORDERS) == null || config.get(BORDERS).isEmpty()) {
            throw new IllegalStateException("trying to create a NumberFacet without configuration");
        }
        thresholds = setThresholdsFromConfiguration(config);
    }

    @Override
    public FacetBuckets bucketsFrom(Field field, List<?> list) {

        Map<String, AtomicInteger> buckets = new HashMap<>();
        Map<String, String> names = new HashMap<>();

        IntStream.rangeClosed(0, thresholds.size()).forEach(i -> { // NOSONAR
            buckets.put(String.valueOf(i), new AtomicInteger());
            if (i == 0) {
                names.put(String.valueOf(i), " x < " + thresholds.get(i));
            } else if (i < thresholds.size()) {
                names.put(String.valueOf(i), thresholds.get(i - 1) + " < x < " + thresholds.get(i));
            } else {
                names.put(String.valueOf(i), " x > " + thresholds.get(i - 1));
            }
        });

        list.stream()
                .forEach(t -> {
                    try {
                        Double object = (double) field.get(t);
                        boolean found = false;
                        for (int i = 0; i < thresholds.size(); i++) {
                            if (object < thresholds.get(i)) {
                                buckets.get(String.valueOf(i)).incrementAndGet();
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            buckets.get(thresholds.size()).incrementAndGet();
                        }
                    } catch (Exception e) { // NOSONAR
                        log.error(e.getMessage());
                    }
                });

        return new FacetBuckets(field.getName(), buckets, names, "");
    }

    @Override
    public String sqlFilterExpression(String value, String operatorSign) {
        Integer parsedInt = Double.valueOf(value).intValue();

        if (parsedInt == 0) {
            Double borderValue = thresholds.get(parsedInt);
            return new StringBuilder(getName()).append("<").append(borderValue).toString();
        } else if (parsedInt >= thresholds.size()) {
            Double borderValue = thresholds.get(thresholds.size() - 1);
            return new StringBuilder(getName()).append(">").append(borderValue).toString();
        } else {
            return new StringBuilder()
                .append(getName()).append(">").append(thresholds.get(parsedInt - 1))
                .append(" AND ")
                .append(getName()).append("<").append(thresholds.get(parsedInt))
                .toString();
        }
    }

    @Override
    public boolean match(ExprNode node, Object gotten, String value) {
        if (!(gotten instanceof Comparable)) {
            return false;
        }
        return node.evaluateValue(gotten);
    }

    @Override
    public Set<String> getSelected(String value) {
        Set<String> result = new HashSet<>();
        IntStream.range(0, thresholds.size()) // NOSONAR
            .forEach(idx -> {
                Double t = thresholds.get(idx);
                if (t.toString().equals(value)) {
                    result.add(Integer.toString(idx));
                }
            });
        return result;
    }

    private List<Double> setThresholdsFromConfiguration(Map<String, String> config) {
        return Arrays.stream(config.get(BORDERS).split(",")) // NOSONAR
                .map(t -> t.trim())
                .map(Double::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public Form addFormParameters(Form newForm, String fieldname, String format, String bucketIdAsString) {
        int bucketId = Integer.parseInt(bucketIdAsString);
        if (bucketId == 0) {
            newForm.add(
                    new Parameter(FilterParamUtils.FILTER_PARAM_KEY,
                            "(" + fieldname + format + "<" + thresholds.get(bucketId) + ")"));
        } else if (bucketId == thresholds.size()) {
            newForm.add(new Parameter(FilterParamUtils.FILTER_PARAM_KEY,
                    "(" + fieldname + format + ">" + thresholds.get(bucketId - 1) + ")"));
        } else {
            newForm.add(new Parameter(FilterParamUtils.FILTER_PARAM_KEY,
                    "(&(" + fieldname + format + ">" + thresholds.get(bucketId - 1)
                            + ")(" + fieldname + format + "<" + thresholds.get(bucketId) + "))"));
        }
        return newForm;
    }

}
