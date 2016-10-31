package io.skysail.server.queryfilter;

import java.util.Map;

import io.skysail.server.domain.jvm.FieldFacet;

public interface ExprNode {

    boolean isLeaf();

    Operation getOperation();

    Object accept( FilterVisitor visitor );

	PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor, Map<String, FieldFacet> facets);

	boolean evaluateEntity(EntityEvaluationVisitor entityEvaluationVisitor, Map<String, FieldFacet> facets);

}
