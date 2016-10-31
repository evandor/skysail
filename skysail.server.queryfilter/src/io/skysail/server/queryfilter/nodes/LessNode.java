package io.skysail.server.queryfilter.nodes;

import java.util.Map;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.queryfilter.EntityEvaluationVisitor;
import io.skysail.server.queryfilter.Operation;
import io.skysail.server.queryfilter.PreparedStatement;
import io.skysail.server.queryfilter.SqlFilterVisitor;
import lombok.ToString;

@ToString(callSuper = true)
public class LessNode extends LeafNode {

    public LessNode(String attribute, String value) {
        super(Operation.LESS, attribute, value);
    }

    @Override
	public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor, Map<String, FieldFacet> facets) {
    	PreparedStatement ps = new PreparedStatement();
		if (getValue().contains("(")) {
			ps.append(getAttribute()).append(" < ").append(getValue());
		} else {
			ps.append(getAttribute()).append("<:").append(getAttribute());
			ps.put(getAttribute(), getValue());
		}
		return ps;

    }

	@Override
	public boolean evaluateEntity(EntityEvaluationVisitor entityEvaluationVisitor, Map<String, FieldFacet> facets) {
		// TODO Auto-generated method stub
		return false;
	}
}
