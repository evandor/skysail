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

@Getter
public class NumberFacet extends FieldFacet {

	private String value;

	public NumberFacet(Field f, String value) {
		this.field = f;
		this.value = value;
		this.field.setAccessible(true);
	}

	@Override
	public Map<Integer, AtomicInteger> bucketsFrom(List<?> list) {
		List<Integer> threasholds = new ArrayList<>();
		Arrays.stream(value.split(",")).forEach(v -> {
			threasholds.add(Integer.parseInt(v));
		});

		Map<Integer, AtomicInteger> buckets = new HashMap<>();
		
		IntStream.rangeClosed(0, threasholds.size()).forEach(i -> buckets.put(i, new AtomicInteger()));

		list.stream() 
			.forEach(t -> {
				try {
					Double object = (double) field.get(t);
					if (object < threasholds.get(0)) {
						buckets.get(0).incrementAndGet();
					} else if (object < threasholds.get(1)) {
						buckets.get(1).incrementAndGet();
					} else if (object < threasholds.get(2)) {
						buckets.get(2).incrementAndGet();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

		return buckets;
	}

}
