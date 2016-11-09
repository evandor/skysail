package io.skysail.server.queryfilter.nodes;

import java.util.List;
import java.util.stream.Collectors;

import io.skysail.server.filter.EntityEvaluationFilterVisitor;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;

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
        for (ExprNode childNode : childList) {
            if (!childNode.evaluateEntity(entityEvaluationVisitor)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ExprNode reduce(String value, String format) {
        AndNode andNode = new AndNode();
        for (ExprNode childNode : childList) {
            if (!isMatchingLeafNode(value, childNode)) {
                andNode.childList.add(childNode);
            }
        }
        if (andNode.getChildList().isEmpty()) {
            return new NullNode();
        }
        if (andNode.getChildList().size() == 1) {
            return andNode.getChildList().get(0);
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
