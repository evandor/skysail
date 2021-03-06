package io.skysail.server.filter;

import java.util.Map;

import io.skysail.domain.Entity;
import io.skysail.server.domain.jvm.FieldFacet;
import lombok.Getter;

@Getter
public class EntityEvaluationFilterVisitor implements FilterVisitor {

    private Map<String, FieldFacet> facets;
    private Entity t;

    public EntityEvaluationFilterVisitor(Entity t, Map<String, FieldFacet> facets) {
        this.t = t;
        this.facets = facets;
    }

    @Override
    public Object visit(ExprNode node) {
        return node.evaluateEntity(this);
    }
}
