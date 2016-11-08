package io.skysail.server.queryfilter.nodes;

import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.FilterVisitor;
import io.skysail.server.filter.Operation;
import lombok.Getter;
import lombok.NonNull;

/**
 * Each Implementor of the class AbstactExprNode has an associated
 * Operation, passed in the constructor.
 *
 */
public abstract class AbstractExprNode implements ExprNode {

    @Getter
    private final Operation operation;

    public AbstractExprNode(@NonNull Operation op) {
        this.operation = op;
    }

    @Override
    public final Object accept(FilterVisitor visitor) {
        return visitor.visit(this);
    }
}
