package io.skysail.server.queryfilter.nodes;

import java.util.List;
import java.util.stream.Collectors;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.filter.EntityEvaluationFilterVisitor;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;

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
    public ExprNode reduce(String value, FieldFacet facet, String format) {
        OrNode orNode = new OrNode();
        for (ExprNode childNode : childList) {
            if (!isMatchingNode(value, childNode)) {
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
    public String asLdapString() {
        StringBuilder sb = new StringBuilder("(|");
        for (ExprNode exprNode : childList) {
            sb.append(exprNode.asLdapString());
        }
        return sb.append(")").toString();
    }

    private boolean isMatchingNode(String value, ExprNode exprNode) {
        return value.equals(exprNode.asLdapString());
    }

}
