package io.skysail.server.queryfilter;

import java.util.Map;

import io.skysail.server.domain.jvm.FieldFacet;

public class EntityEvaluationVisitor implements FilterVisitor {

	private Map<String, FieldFacet> facets;

	public EntityEvaluationVisitor(Map<String, FieldFacet> facets) {
		this.facets = facets;
	}

	@Override
	public Object visit(ExprNode node) {
		return null;
	}
}
