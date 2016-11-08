package io.skysail.server.queryfilter.nodes;

import java.util.Arrays;

import io.skysail.server.filter.EntityEvaluationFilterVisitor;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;
import lombok.ToString;

@ToString(callSuper = true)
public class NotNode extends BranchNode {

    public NotNode(ExprNode child) {
        super(Operation.NOT, Arrays.asList(child));
    }

    public ExprNode getChild() {
        return this.childList.get(0);
    }

    @Override
    public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor) {
        return new PreparedStatement("NOT", Arrays.asList(sqlFilterVisitor.visit(getChild())));
    }

    @Override
    public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor) {
        return !(Boolean) entityEvaluationVisitor.visit(getChild());
    }

    @Override
    public ExprNode reduce(String value, String format) {
        if (!isMatchingLeafNode(value, getChild())) {
            return this;
        }
        return new NullNode();
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder("(!");
        sb.append(this.childList.get(0).render());
        return sb.append(")").toString();
    }

    private boolean isMatchingLeafNode(String value, ExprNode exprNode) {
        return exprNode.isLeaf() && value.equals(((LeafNode) exprNode).getValue());
    }

}
