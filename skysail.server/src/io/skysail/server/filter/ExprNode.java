package io.skysail.server.filter;

import java.util.Set;

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

    Set<String> getSelected();

    Set<String> getKeys();

    ExprNode reduce(String value, String format);

    String render();

    PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor);

    boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor);

    boolean evaluateValue(Object gotten);

}
