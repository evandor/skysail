package io.skysail.server.domain.jvm.facets;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.skysail.server.domain.jvm.FieldFacet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class YearFacet extends FieldFacet {

    private static final String START = "START";
    private static final String END = "END";

    private String value;
    private Integer start;
    private Integer end;

    public YearFacet(Map<String,String> config) {
        super(config);
        start = getBorder(START, config);
        end = getBorder(END, config);
    }

    private Integer getBorder(String key, Map<String, String> config) {
        if (config.get(key) == null || "".equals(config.get(key).trim())) {
            return new Date().getYear() + 1900;
        } else {
            return Integer.parseInt(config.get(key));
        }
    }

    @Override
    public Map<Integer, AtomicInteger> bucketsFrom(List<?> list) {
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


}
