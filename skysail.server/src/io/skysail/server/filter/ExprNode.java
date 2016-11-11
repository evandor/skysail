package io.skysail.server.filter;

import java.util.Map;
import java.util.Set;

import io.skysail.server.domain.jvm.FieldFacet;

/**
 * ExprNodes are used to model, parse and render LDAP-style filter expressions
 * like (&(A=a)(B=b)).
 *
 * There are leaf-nodes (without children) and nodes with children, and each
 * type of node has one associated operation (Like "EUQAL", "AND", "OR", etc).
 *
 */
public interface ExprNode {

    boolean isLeaf();

    Operation getOperation();

    Object accept(FilterVisitor visitor);

    Set<String> getSelected(FieldFacet facet, Map<String, String> lines);

    Set<String> getKeys();

    ExprNode reduce(String value, FieldFacet facet, String format);

    String render();

    PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor);

    boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor);

    boolean evaluateValue(Object gotten);

}
