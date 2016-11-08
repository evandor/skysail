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
public class OrNode extends BranchNode {

    public OrNode() {
        super(Operation.OR);
    }

    public OrNode(List<ExprNode> childList) {
        super(Operation.OR, childList);
    }

    public Object getChildren() {
        return null;
    }

    @Override
    public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor) {
        List<PreparedStatement> collect = getChildList().stream()
                .map(sqlFilterVisitor::visit)
                .collect(Collectors.toList());
        return new PreparedStatement("OR", collect);
    }

    @Override
    public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor) {
        for (ExprNode childNode : childList) {
            if (childNode.evaluateEntity(entityEvaluationVisitor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ExprNode reduce(String value, String format) {
        OrNode orNode = new OrNode();
        for (ExprNode childNode : childList) {
            if (!isMatchingLeafNode(value, childNode)) {
                orNode.childList.add(childNode);
            }
        }
        if (orNode.getChildList().isEmpty()) {
            return new NullNode();
        }
        if (orNode.getChildList().size() == 1) {
            return orNode.getChildList().get(0);
        }
        return orNode;
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder("(|");
        for (ExprNode exprNode : childList) {
            sb.append(exprNode.render());
        }
        return sb.append(")").toString();
    }

    private boolean isMatchingLeafNode(String value, ExprNode exprNode) {
        return exprNode.isLeaf() && value.equals(((LeafNode) exprNode).getValue());
    }

}
