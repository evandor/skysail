package io.skysail.server.domain.jvm.facets;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.skysail.server.domain.jvm.FieldFacet;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ToString(callSuper = true)
public class YearFacet extends FieldFacet {

    private static final String START = "START";
    private static final String END = "END";

    private String value;
    private Integer start;
    private Integer end;

    public YearFacet(String id, Map<String,String> config) {
        super(id, config);
        start = getBorder(START, config);
        end = getBorder(END, config);
    }

    private Integer getBorder(String key, Map<String, String> config) {
        String theValue = config.get(key);
		if (theValue == null || "".equals(theValue.trim()) || "NOW".equals(theValue)) {
            return new Date().getYear() + 1900;
        } else {
            return Integer.parseInt(theValue);
        }
    }

    @Override
    public Map<Integer, AtomicInteger> bucketsFrom(Field field, List<?> list) {
        Map<Integer, AtomicInteger> buckets = new HashMap<>();
        list.stream()
                .forEach(t -> {
                    try {
                        Date date = (Date) field.get(t);
                        @SuppressWarnings("deprecation")
                        int year = (1900 + date.getYear());
                        if (!buckets.containsKey(year)) {
                            buckets.put(year, new AtomicInteger());
                        }
                        buckets.get(year).incrementAndGet();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                });

        return buckets;
    }

    @Override
    public String sqlFilterExpression(String value) {
        return new StringBuilder(getName()).append(".format('YYYY')").append("=:").append(getName()).toString();
    }

}
