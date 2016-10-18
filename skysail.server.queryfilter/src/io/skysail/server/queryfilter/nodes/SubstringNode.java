package io.skysail.server.queryfilter.nodes;

import java.util.Map;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.queryfilter.Operation;
import io.skysail.server.queryfilter.PreparedStatement;
import io.skysail.server.queryfilter.SqlFilterVisitor;
import lombok.ToString;

@ToString
public class SubstringNode extends LeafNode {

    public SubstringNode(String attribute, String value) {
        super(Operation.SUBSTRING, attribute, value);
    }
    
    @Override
	public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor, Map<String, FieldFacet> facets) {
		PreparedStatement ps = new PreparedStatement();
		ps.append(getAttribute()).append(" containstext '").append(getValue()).append("'");
		return ps;
    }

}
