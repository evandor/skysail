package io.skysail.server.domain.jvm;

import io.skysail.domain.lists.Facet;
import io.skysail.domain.lists.FacetType;
import lombok.Getter;

@Getter
public class FieldFacet {

    private FacetType type;
    private String value;
    private Class<?> cls;

    public FieldFacet(Class<?> cls, Facet facetAnnotation) {
        this.cls = cls;
        type = facetAnnotation.type();
        value = facetAnnotation.value();
    }

}
