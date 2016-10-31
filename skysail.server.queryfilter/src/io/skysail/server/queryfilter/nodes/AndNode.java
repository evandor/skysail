package io.skysail.server.queryfilter.nodes;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.queryfilter.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.ToString;

@ToString
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
	public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor, Map<String, FieldFacet> facets) {
		return new PreparedStatement("AND",getChildList().stream().map(sqlFilterVisitor::visit).collect(Collectors.toList()));
	}

	@Override
	public boolean evaluateEntity(EntityEvaluationVisitor entityEvaluationVisitor, Map<String, FieldFacet> facets) {
		for (ExprNode exprNode : childList) {
			if (!exprNode.evaluateEntity(entityEvaluationVisitor, facets)) {
				return false;
			}
		}
		return true;
	}

}
