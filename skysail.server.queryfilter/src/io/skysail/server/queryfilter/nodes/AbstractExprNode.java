package io.skysail.server.queryfilter.nodes;

import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.FilterVisitor;
import io.skysail.server.filter.Operation;

public abstract class AbstractExprNode implements ExprNode {

    private Operation operation;

    public AbstractExprNode(Operation op) {
        this.operation = op;
    }

    @Override
    public final Operation getOperation() {
        return this.operation;
    }

    @Override
    public final Object accept(FilterVisitor visitor) {
        return visitor.visit(this);
    }
}
