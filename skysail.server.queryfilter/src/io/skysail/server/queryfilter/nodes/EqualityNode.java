package io.skysail.server.queryfilter.nodes;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.domain.jvm.facets.NumberFacet;
import io.skysail.server.filter.EntityEvaluationFilterVisitor;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EqualityNode extends LeafNode {

    public EqualityNode(String attribute, String value) {
        super(Operation.EQUAL, attribute, value);
    }

    @Override
    public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor) {
        PreparedStatement ps = new PreparedStatement();
        String attributeName = getAttribute();

        Map<String, FieldFacet> facets = sqlFilterVisitor.getFacets();
        if (facets.containsKey(attributeName)) {
            ps.append(facets.get(attributeName).sqlFilterExpression(getValue(),"=:"));
            ps.put(attributeName, getValue());
        } else {
            ps.append(attributeName).append("=:").append(attributeName);
            ps.put(attributeName, getValue());
        }
        return ps;
    }

    @Override
    public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor) {
        String attributeName;
        String format = null;
        int semicolonPosition = getAttribute().indexOf(";");
        if (semicolonPosition < 0) {
            attributeName = getAttribute();
        } else {
            attributeName = getAttribute().substring(0, semicolonPosition);
            format = getAttribute().substring(semicolonPosition + 1);
        }
        Identifiable t = entityEvaluationVisitor.getT();
        Map<String, FieldFacet> facets = entityEvaluationVisitor.getFacets();
        try {
            String getterName = "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
            Method getter = t.getClass().getDeclaredMethod(getterName);
            Object gotten = getter.invoke(t);
            if (facets.containsKey(attributeName)) {
                FieldFacet fieldFacet = facets.get(attributeName);

                if (fieldFacet instanceof NumberFacet) {
                    fieldFacet.match(this,gotten,getValue());
                }
                //String sqlFilterExpression = fieldFacet.sqlFilterExpression(gotten.toString());
                if (format == null || "".equals(format.trim())) {
                    return false;
                }

                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.format((Date) gotten).equals(getValue());
            } else {
                return getValue().equals(gotten);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public Set<String> getSelected() {
        Set<String> result = new HashSet<>();
        result.add(getValue());
        return result;
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(getAttribute()).append("=").append(getValue());
        return sb.append(")").toString();
    }

    @Override
    public ExprNode reduce(String value, String format) {
        if (getValue().equals(value)) {
            return new NullNode();
        }
        return this;
    }

}
