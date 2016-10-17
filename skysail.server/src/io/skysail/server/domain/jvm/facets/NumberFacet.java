package io.skysail.server.domain.jvm.facets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import io.skysail.server.domain.jvm.FieldFacet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class NumberFacet extends FieldFacet {

    private static final String BORDERS = "BORDERS";

    private String value;

    private List<Integer> thresholds;

    public NumberFacet(Map<String,String> config) {
        super(config);
        String borders = config.get(BORDERS);
        thresholds = new ArrayList<>();
        Arrays.stream(borders.split(",")).forEach(v -> {
            thresholds.add(Integer.parseInt(v));
        });

    }


    @Override
    public Map<Integer, AtomicInteger> bucketsFrom(List<?> list) {

        Map<Integer, AtomicInteger> buckets = new HashMap<>();

        IntStream.rangeClosed(0, thresholds.size()).forEach(i -> buckets.put(i, new AtomicInteger()));

        list.stream()
                .forEach(t -> {
                    try {
                        Double object = (double) field.get(t);
                        boolean found = false;
                        for (int i = 0; i < thresholds.size(); i++) {
                            if (object < thresholds.get(i)) {
                                buckets.get(i).incrementAndGet();
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

}
