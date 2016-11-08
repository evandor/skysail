package io.skysail.server.queryfilter.nodes;

import io.skysail.server.filter.EntityEvaluationFilterVisitor;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;
import lombok.ToString;

@ToString(callSuper = true)
public class LessNode extends LeafNode {

    public LessNode(String attribute, String value) {
        super(Operation.LESS, attribute, value);
    }

    @Override
    public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor) {
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
    public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor) {
        return false;
    }

    @Override
    public String render() {
        StringBuilder sb = new StringBuilder("(");
        sb.append(getAttribute()).append(" > ").append(getValue());
        return sb.append(")").toString();
    }

}
