package io.skysail.server.domain.jvm;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import io.skysail.domain.lists.Facet;
import io.skysail.server.domain.jvm.facets.NoOpFacet;
import io.skysail.server.domain.jvm.facets.NumberFacet;
import io.skysail.server.domain.jvm.facets.YearFacet;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class FieldFacet {

	protected Field field;

	public static FieldFacet createFor(Field f, Facet facetAnnotation) {
		switch (facetAnnotation.type()) {
		case NUMBER:
			return new NumberFacet(f, facetAnnotation.value());
		case YEAR:
			return new YearFacet(f, facetAnnotation.value());
		default:
			return new NoOpFacet(f);
		}
	}

	public abstract Map<Integer, AtomicInteger> bucketsFrom(List<?> list);

}
