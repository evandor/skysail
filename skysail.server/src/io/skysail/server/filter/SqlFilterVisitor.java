package io.skysail.server.filter;

import java.util.Map;

import io.skysail.server.domain.jvm.FieldFacet;
import lombok.Getter;

public class SqlFilterVisitor implements FilterVisitor {

    @Getter
    private Map<String, FieldFacet> facets;

    public SqlFilterVisitor(Map<String, FieldFacet> facets) {
        this.facets = facets;
    }

    @Override
    public PreparedStatement visit(ExprNode exprNode) {
        return exprNode.createPreparedStatement(this);
    }

}
