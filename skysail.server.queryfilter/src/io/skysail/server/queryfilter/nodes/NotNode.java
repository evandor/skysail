package io.skysail.server.queryfilter.nodes;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.queryfilter.*;

import java.util.Arrays;
import java.util.Map;

import lombok.ToString;

@ToString
public class NotNode extends BranchNode {

    public NotNode(ExprNode child) {
        super(Operation.NOT, Arrays.asList(child));
    }

    public ExprNode getChild() {
        return this.childList.get(0);
    }

    @Override
	public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor, Map<String, FieldFacet> facets) {
		return new PreparedStatement("NOT", Arrays.asList(sqlFilterVisitor.visit(getChild())));

    }
}
