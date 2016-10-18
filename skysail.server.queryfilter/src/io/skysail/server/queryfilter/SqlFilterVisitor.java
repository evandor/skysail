package io.skysail.server.queryfilter;

import java.util.Map;

import io.skysail.server.domain.jvm.FieldFacet;

public class SqlFilterVisitor implements FilterVisitor {

	private Map<String, FieldFacet> facets;

	public SqlFilterVisitor(Map<String, FieldFacet> facets) {
		this.facets = facets;
	}

	@Override
	public PreparedStatement visit(ExprNode exprNode) {
		return exprNode.createPreparedStatement(this, facets);
	}
}
