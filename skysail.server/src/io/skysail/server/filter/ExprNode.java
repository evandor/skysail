package io.skysail.server.filter;

import java.util.Map;
import java.util.Set;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;

public interface ExprNode {

    boolean isLeaf();

    Operation getOperation();

    Object accept( FilterVisitor visitor );

	PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor, Map<String, FieldFacet> facets);

	boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor, Identifiable t, Map<String, FieldFacet> facets);

    Set<String> getSelected();

    Set<String> getKeys();

}
