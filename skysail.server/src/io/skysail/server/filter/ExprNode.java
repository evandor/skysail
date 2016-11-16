package io.skysail.server.filter;

import java.util.Map;
import java.util.Set;

import io.skysail.server.domain.jvm.FieldFacet;

/**
 * ExprNodes are used to model, parse and render LDAP-style filter expressions
 * like (&(A=a)(B=b)).
 *
 * There are leaf-nodes (without children) and nodes with children, and each
 * type of node has one associated operation (Like "EQUAL", "AND", "OR", etc).
 *
 */
public interface ExprNode {

    /**
     * Traverse the node by applying the provided filter to each subnode.
     *
     * @param visitor
     * @return the result.
     */
    Object accept(FilterVisitor visitor);

    /**
     * @return true if this is a type of node which cannot have children.
     */
    boolean isLeaf();

    /**
     * @return the (root) operation of this node (like "AND", "OR", "LESS", ...)
     */
    Operation getOperation();

    /**
     * @return a LDAP-style string representation of this node (like "(&(a=b)(x=y))");
     */
    String asLdapString();

    Set<String> getSelected(FieldFacet facet, Map<String, String> lines);

    Set<String> getKeys();

    ExprNode reduce(String value, FieldFacet facet, String format);

    /**
     * @param sqlFilterVisitor
     * @return a PreparedStatement representation of this node, which can be rendered as a SQL Where Statement;
     */
    PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor);

    boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor);

    boolean evaluateValue(Object gotten);

}
