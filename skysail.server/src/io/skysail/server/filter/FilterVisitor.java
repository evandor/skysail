package io.skysail.server.queryfilter;


public interface FilterVisitor {

    Object visit( ExprNode node );

}
