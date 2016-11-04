package io.skysail.server.queryfilter.nodes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
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

    public Object getChildren() {
        return null;
    }

    @Override
    public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor,
            Map<String, FieldFacet> facets) {
        List<PreparedStatement> collect = getChildList().stream()
                .map(sqlFilterVisitor::visit)
                .map(PreparedStatement.class::cast)
                .collect(Collectors.toList());
        return new PreparedStatement("AND",collect);
    }

    @Override
    public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor, Identifiable t, Map<String, FieldFacet> facets) {
        for (ExprNode exprNode : childList) {
            if (!exprNode.evaluateEntity(entityEvaluationVisitor, t, facets)) {
                return false;
            }
        }
        return true;
    }

}
