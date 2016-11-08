package io.skysail.server.queryfilter.nodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.Operation;
import lombok.Getter;
import lombok.ToString;

/**
 * A BranchNode is an ExprNode with children.
 *
 * The associated operation (like "AND") is applied to all children. "isLeaf"
 * will always return false.
 *
 */
@ToString
public abstract class BranchNode extends AbstractExprNode {

    @Getter
    protected List<ExprNode> childList = new ArrayList<>();

    protected BranchNode(Operation op) {
        super(op);
    }

    public BranchNode(Operation op, List<ExprNode> childList) {
        super(op);
        this.childList = childList;
    }

    @Override
    public final boolean isLeaf() {
        return false;
    }

    @Override
    public Set<String> getSelected() {
        Set<String> result = new HashSet<>();
        for (ExprNode exprNode : childList) {
            result.addAll(exprNode.getSelected());
        }
        return result;
    }

    @Override
    public ExprNode reduce(String value, String format) {
        return this;
    }

    @Override
    public Set<String> getKeys() {
        Set<String> result = new HashSet<>();
        for (ExprNode exprNode : childList) {
            result.addAll(exprNode.getKeys());
        }
        return result;
    }



}
