package io.skysail.server.queryfilter.nodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.Operation;
import lombok.Getter;

/**
 * A BranchNode is an ExprNode with children.
 *
 * The associated operation (like "AND") is applied to all children. "isLeaf"
 * will always return false.
 *
 */
public abstract class BranchNode extends AbstractExprNode {

    @Getter
    protected List<ExprNode> childList = new ArrayList<>();

    protected BranchNode(Operation op) {
        super(op);
    }

    public BranchNode(Operation op, List<ExprNode> childList) {
        super(op);
        this.childList = childList;
    }

    @Override
    public final boolean isLeaf() {
        return false;
    }

    @Override
    public Set<String> getSelected(FieldFacet facet, Map<String, String> lines) {
        Set<String> result = new HashSet<>();

        lines.keySet().forEach(key -> {
            if (lines.get(key).equals(asLdapString())) { // "(betrag<0.0)"; "(|(betrag<0.0)(&(betrag>0.0)(betrag<100.0)))"
                result.add(key);
            }
        });


        for (ExprNode child : childList) {
           result.addAll(child.getSelected(facet, lines));
        }
        return result;
    }

    @Override
    public ExprNode reduce(String value, FieldFacet facet,String format) {
        return this;
    }

    @Override
    public Set<String> getKeys() {
        Set<String> result = new HashSet<>();
        for (ExprNode exprNode : childList) {
            result.addAll(exprNode.getKeys());
        }
        return result;
    }

    @Override
    public boolean evaluateValue(Object gotten) {
        // TODO Auto-generated method stub
        return false;
    }



}
