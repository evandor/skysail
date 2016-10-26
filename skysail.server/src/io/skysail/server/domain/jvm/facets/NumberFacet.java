package io.skysail.server.domain.jvm.facets;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import io.skysail.server.domain.jvm.FieldFacet;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ToString(callSuper = true)
public class NumberFacet extends FieldFacet {

    private static final String BORDERS = "BORDERS";

    private String value;

    private List<Integer> thresholds;

    public NumberFacet(String id, Map<String, String> config) {
        super(id, config);
        String borders = config.get(BORDERS);
        thresholds = new ArrayList<>();
        Arrays.stream(borders.split(",")).forEach(v -> thresholds.add(Integer.parseInt(v)));
    }

    @Override
    public Map<String, AtomicInteger> bucketsFrom(Field field, List<?> list) {

        Map<String, AtomicInteger> buckets = new HashMap<>();

        IntStream.rangeClosed(0, thresholds.size()).forEach(i -> buckets.put(String.valueOf(i), new AtomicInteger()));

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
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });

        return buckets;
    }

    @Override
    public String sqlFilterExpression(String value) {
        int parsedInt = Integer.parseInt(value);
        if (parsedInt == 0) {
            Integer borderValue = thresholds.get(parsedInt);
            return new StringBuilder(getName()).append("<").append(borderValue).toString();
        } else if (parsedInt >= thresholds.size()) {
            Integer borderValue = thresholds.get(thresholds.size()-1);
            return new StringBuilder(getName()).append(">").append(borderValue).toString();
        } else {
            return new StringBuilder()
                .append(getName()).append(">").append(thresholds.get(parsedInt-1))
                .append(" AND ")
                .append(getName()).append("<").append(thresholds.get(parsedInt))
                .toString();
        }
    }

}
