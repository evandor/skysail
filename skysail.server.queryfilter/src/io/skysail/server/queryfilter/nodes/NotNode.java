package io.skysail.server.queryfilter.nodes;

import java.util.Arrays;
import java.util.Map;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.queryfilter.EntityEvaluationFilterVisitor;
import io.skysail.server.queryfilter.ExprNode;
import io.skysail.server.queryfilter.Operation;
import io.skysail.server.queryfilter.PreparedStatement;
import io.skysail.server.queryfilter.SqlFilterVisitor;
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
	public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor, Map<String, FieldFacet> facets) {
		return new PreparedStatement("NOT", Arrays.asList((PreparedStatement)sqlFilterVisitor.visit(getChild())));
    }

    @Override
	public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor,Identifiable t,  Map<String, FieldFacet> facets) {
    	return !(Boolean)entityEvaluationVisitor.visit(getChild());
    }
}
