package io.skysail.server.facets;

import java.util.Map;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.domain.jvm.facets.MatcherFacet;
import io.skysail.server.domain.jvm.facets.NumberFacet;
import io.skysail.server.domain.jvm.facets.YearFacet;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum FacetType {

    NUMBER(NumberFacet.class),
	YEAR(YearFacet.class),
	MATCH(MatcherFacet.class),
	MONTH(YearFacet.class);

	@Getter
    private Class<? extends FieldFacet> implementor;

    private FacetType(Class<? extends FieldFacet> implementor) {
        this.implementor = implementor;
    }

    public FieldFacet fromConfig(String ident, Map<String, String> config) {
        try {
            return implementor.getConstructor(new Class[]{String.class, Map.class}).newInstance(ident, config);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }
}
