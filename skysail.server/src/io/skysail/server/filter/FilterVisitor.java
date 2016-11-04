package io.skysail.server.filter;

public interface FilterVisitor {

    Object visit( ExprNode node );

}
