package io.skysail.server.queryfilter.nodes;

import io.skysail.server.filter.EntityEvaluationFilterVisitor;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;

public class NullNode extends LeafNode {

    protected NullNode() {
        super(Operation.NONE);
    }

    @Override
    public String render() {
        return "";
    }

    @Override
    public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor) {
        return new PreparedStatement();
    }

    @Override
    public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor) {
        return true;
    }

}
