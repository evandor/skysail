package io.skysail.server.queryfilter.nodes;

import java.util.Map;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.queryfilter.Operation;
import io.skysail.server.queryfilter.PreparedStatement;
import io.skysail.server.queryfilter.SqlFilterVisitor;
import lombok.ToString;

@ToString
public class EqualityNode extends LeafNode {

    public EqualityNode(String attribute, String value) {
        super(Operation.EQUAL, attribute, value);
    }

    @Override
    public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor,
            Map<String, FieldFacet> facets) {
        PreparedStatement ps = new PreparedStatement();
        String attributeName = getAttribute();
        if (facets.containsKey(attributeName)) {
            ps.append(facets.get(attributeName).sqlFilterExpression(getValue()));
            ps.put(attributeName, getValue());
        } else {
            ps.append(attributeName).append("=:").append(attributeName);
            ps.put(attributeName, getValue());
        }
        return ps;
    }

}
