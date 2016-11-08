package io.skysail.server.queryfilter.nodes;

import java.util.List;
import java.util.stream.Collectors;

import io.skysail.server.filter.EntityEvaluationFilterVisitor;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;
import lombok.ToString;

@ToString(callSuper = true)
public class AndNode extends BranchNode {

    public AndNode() {
        super(Operation.AND);
    }

    public AndNode(List<ExprNode> childList) {
        super(Operation.AND, childList);
    }

    @Override
    public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor) {
        List<PreparedStatement> collect = getChildList().stream()
                .map(sqlFilterVisitor::visit)
                .collect(Collectors.toList());
        return new PreparedStatement("AND", collect);
    }

    @Override
    public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor) {
        for (ExprNode exprNode : childList) {
            if (!exprNode.evaluateEntity(entityEvaluationVisitor)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ExprNode reduce(String value, String format) {
        AndNode andNode = new AndNode();
        for (ExprNode exprNode : childList) {
            if (!isMatchingLeafNode(value, exprNode)) {
                andNode.childList.add(exprNode);
            }
        }
        if (andNode.getChildList().isEmpty()) {
            return new NullNode();
        }
        return andNode;
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder("(&");
        for (ExprNode exprNode : childList) {
            sb.append(exprNode.render());
        }
        return sb.append(")").toString();
    }

    private boolean isMatchingLeafNode(String value, ExprNode exprNode) {
        return exprNode.isLeaf() && value.equals(((LeafNode) exprNode).getValue());
    }

}
