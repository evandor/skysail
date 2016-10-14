package io.skysail.domain.lists;

import lombok.Getter;

public enum FacetType {

    RANGE;

	@Getter
    private String value;

    private FacetType() {
        value = this.name();
    }

    private FacetType(String value) {
        this.value = value;
    }
}
