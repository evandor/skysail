package io.skysail.server.queryfilter.nodes;

import java.util.Map;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.filter.EntityEvaluationFilterVisitor;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;
import lombok.ToString;

@ToString(callSuper = true)
public class IsInNode extends LeafNode {

    public IsInNode(String attribute, String value) {
        super(Operation.IN, attribute, value);
    }

    @Override
    public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor) {
        PreparedStatement ps = new PreparedStatement();
        ps.append(getAttribute()).append(" IN ").append(getValue().replace("[", "(").replace("]", ")"));
        return ps;
    }

    @Override
    public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor) {
        return false;
    }

    @Override
    public String asLdapString() {
        StringBuilder sb = new StringBuilder("(");
        return sb.append(")").toString();
    }

    @Override
    protected boolean handleFacet(String attributeName, String format, Map<String, FieldFacet> facets, Object gotten) {
        return false;
    }

}
