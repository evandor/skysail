package io.skysail.server.queryfilter.nodes;

import java.util.Map;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.queryfilter.EntityEvaluationFilterVisitor;
import io.skysail.server.queryfilter.Operation;
import io.skysail.server.queryfilter.PreparedStatement;
import io.skysail.server.queryfilter.SqlFilterVisitor;
import lombok.ToString;

@ToString(callSuper = true)
public class IsInNode extends LeafNode {

    public IsInNode(String attribute, String value) {
        super(Operation.IN, attribute, value);
    }

    @Override
	public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor, Map<String, FieldFacet> facets) {
    	PreparedStatement ps = new PreparedStatement();
		ps.append(getAttribute()).append(" IN ").append(getValue().replace("[", "(").replace("]", ")"));
		return ps;
    }

	@Override
	public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor, Identifiable t, Map<String, FieldFacet> facets) {
		// TODO Auto-generated method stub
		return false;
	}
}
