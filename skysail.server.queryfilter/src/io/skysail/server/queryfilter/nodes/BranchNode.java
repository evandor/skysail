package io.skysail.server.queryfilter.nodes;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.Operation;
import lombok.Getter;
import lombok.ToString;

@ToString
public abstract class BranchNode extends AbstractExprNode {

    @Getter
    protected List<ExprNode> childList;

    protected BranchNode(Operation op) {
        super(op);
    }

    public BranchNode(Operation op, List<ExprNode> childList) {
        super(op);
        this.childList = childList;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Set<String> getSelected() {
        return Collections.emptySet();
    }

    @Override
    public Set<String> getKeys() {
        return Collections.emptySet();
    }

}
