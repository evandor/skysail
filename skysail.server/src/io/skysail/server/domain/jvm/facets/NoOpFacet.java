package io.skysail.server.domain.jvm.facets;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.skysail.server.domain.jvm.FieldFacet;

public class NoOpFacet extends FieldFacet {

	public NoOpFacet(Field f) {
		this.field = f;
	}

	@Override
	public Map<Integer, AtomicInteger> bucketsFrom(List<?> list) {
		return Collections.emptyMap();
	}

}
