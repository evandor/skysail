package io.skysail.server.queryfilter.nodes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.domain.jvm.facets.NumberFacet;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;

public class GreaterNode extends LeafNode {

    public GreaterNode(String attribute, Number number) {
        super(Operation.GREATER, attribute, number.toString());
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
    public String asLdapString() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(getAttribute()).append(">").append(getValue());
        return sb.append(")").toString();
    }

//    @Override
//    public Set<String> getSelected(FieldFacet facet,Map<String, String> lines) {
//        return facet.getSelected(getValue());
//    }

    @Override
    public Set<String> getKeys() {
        Set<String> result = new HashSet<>();
        String attributeWithoutFormat = getAttribute().split(";",2)[0];
        result.add(attributeWithoutFormat);
        return result;
    }

    @Override
    public ExprNode reduce(String value, FieldFacet facet, String format) {
        if (value.equals(asLdapString())) {
            return new NullNode();
        }
        return this;
    }

    @Override
    public boolean evaluateValue(Object gotten) {
        if (!Number.class.isAssignableFrom(gotten.getClass())) {
            return false;
        }
        Double a = Double.valueOf(getValue());
        return (a.compareTo((Double) gotten)) < 0;
    }

    @Override
    protected boolean handleFacet(String attributeName, String format, Map<String, FieldFacet> facets, Object gotten) {
        FieldFacet fieldFacet = facets.get(attributeName);

        if (fieldFacet instanceof NumberFacet) {
            return fieldFacet.match(this, gotten, getValue());
        }
        // String sqlFilterExpression =
        // fieldFacet.sqlFilterExpression(gotten.toString());
        if (format == null || "".equals(format.trim())) {
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format((Date) gotten).equals(getValue());

    }
}
