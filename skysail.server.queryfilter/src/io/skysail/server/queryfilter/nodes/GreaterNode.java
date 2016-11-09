package io.skysail.server.queryfilter.nodes;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;

public class GreaterNode extends LeafNode {

    public GreaterNode(String attribute, String value) {
        super(Operation.GREATER, attribute, value);
    }

    @Override
    public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor) {
        PreparedStatement ps = new PreparedStatement();
        String attributeName = getAttribute();

        Map<String, FieldFacet> facets = sqlFilterVisitor.getFacets();
        if (facets.containsKey(attributeName)) {
            ps.append(facets.get(attributeName).sqlFilterExpression(getValue(),">:"));
            ps.put(attributeName, getValue());
        } else {
            ps.append(attributeName).append(">:").append(attributeName);
            ps.put(attributeName, getValue());
        }
        return ps;
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(getAttribute()).append(">").append(getValue());
        return sb.append(")").toString();
    }

    @Override
    public Set<String> getSelected() {
        Set<String> result = new HashSet<>();
        result.add(getValue());
        return result;
    }

    @Override
    public Set<String> getKeys() {
        Set<String> result = new HashSet<>();
        String attributeWithoutFormat = getAttribute().split(";",2)[0];
        result.add(attributeWithoutFormat);
        return result;
    }

    @Override
    public ExprNode reduce(String value, String format) {
        if (getValue().equals(value)) {
            return new NullNode();
        }
        return this;
    }

    @Override
    protected boolean handleFacet(String attributeName, String format, Map<String, FieldFacet> facets, Object gotten) {
        return false;
    }
}
