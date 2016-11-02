package io.skysail.server.queryfilter.nodes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.queryfilter.EntityEvaluationFilterVisitor;
import io.skysail.server.queryfilter.ExprNode;
import io.skysail.server.queryfilter.Operation;
import io.skysail.server.queryfilter.PreparedStatement;
import io.skysail.server.queryfilter.SqlFilterVisitor;
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
    public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor,
            Map<String, FieldFacet> facets) {
        List<PreparedStatement> collect = getChildList().stream()
                .map(sqlFilterVisitor::visit)
                .map(PreparedStatement.class::cast)
                .collect(Collectors.toList());
        return new PreparedStatement("OR", collect);
    }

    @Override
    public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor,Identifiable t,  Map<String, FieldFacet> facets) {
        for (ExprNode exprNode : childList) {
            if (exprNode.evaluateEntity(entityEvaluationVisitor, t, facets)) {
                return true;
            }
        }
        return false;
    }

}
