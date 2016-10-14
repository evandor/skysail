package io.skysail.server.domain.jvm.facets;

import java.lang.reflect.Field;
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

	private String value;

	public YearFacet(Field f, String value) {
		this.field = f;
		this.value = value;
		this.field.setAccessible(true);
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
