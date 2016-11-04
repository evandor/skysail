package io.skysail.server.queryfilter.nodes;

import java.util.Map;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.filter.EntityEvaluationFilterVisitor;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;
import lombok.ToString;

@ToString(callSuper = true)
public class PresentNode extends LeafNode {

    public PresentNode(String attribute, String value) {
        super(Operation.PRESENT, attribute, value);
    }

    @Override
	public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor, Map<String, FieldFacet> facets) {
    	PreparedStatement ps = new PreparedStatement();
		ps.append(getAttribute()).append(" is ").append(" NOT NULL");
		return ps;

    }

	@Override
	public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor, Identifiable t, Map<String, FieldFacet> facets) {
		// TODO Auto-generated method stub
		return false;
	}
}
