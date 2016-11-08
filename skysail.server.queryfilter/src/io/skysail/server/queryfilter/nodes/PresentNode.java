package io.skysail.server.queryfilter.nodes;

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
    public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor) {
        PreparedStatement ps = new PreparedStatement();
        ps.append(getAttribute()).append(" is ").append(" NOT NULL");
        return ps;

    }

    @Override
    public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor) {
        return false;
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder("(");
        return sb.append(")").toString();
    }
}
