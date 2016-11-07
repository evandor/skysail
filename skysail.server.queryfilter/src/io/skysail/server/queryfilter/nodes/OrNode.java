package io.skysail.server.queryfilter.nodes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.filter.EntityEvaluationFilterVisitor;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.Operation;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;
import lombok.ToString;

@ToString(callSuper = true)
public class OrNode extends BranchNode {

    public OrNode() {
        super(Operation.OR);
    }

    public OrNode(List<ExprNode> childList) {
        super(Operation.OR, childList);
    }

    public Object getChildren() {
        return null;
    }

    @Override
    public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor,
            Map<String, FieldFacet> facets) {
        List<PreparedStatement> collect = getChildList().stream()
                .map(sqlFilterVisitor::visit)
                .map(PreparedStatement.class::cast)
                .collect(Collectors.toList());
        return new PreparedStatement("OR", collect);
    }

    @Override
    public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor,Identifiable t,  Map<String, FieldFacet> facets) {
        for (ExprNode exprNode : childList) {
            if (exprNode.evaluateEntity(entityEvaluationVisitor, t, facets)) {
                return true;
            }
        }
        return false;
    }

    @Override
	public ExprNode reduce(String value, String format) {
    	LeafNode match = null;
    	for (ExprNode exprNode : childList) {
    		if (isMatchingLeafNode(value, exprNode)) {
    			match = (LeafNode)exprNode;
    		}
    	}
    	if (match == null) {
    		return this;
    	}
    	if (childList.size() > 2) {
    		// remove the match node
    		OrNode orNode = new OrNode();
    		for (ExprNode exprNode : childList) {
        		if (!isMatchingLeafNode(value, exprNode)) {
        			orNode.childList.add(exprNode);
        		}
    		}
    		return orNode;
    	} else {
    		// return the other node
    		for (ExprNode exprNode : childList) {
        		if (!isMatchingLeafNode(value, exprNode)) {
        			return (LeafNode)exprNode;
        		}
        		return exprNode;
        	}
    	}
		return this;
	}
    
	@Override
	public String render() {
		StringBuilder sb = new StringBuilder("(|");
		for (ExprNode exprNode : childList) {
            sb.append(exprNode.render());
        }
		return sb.append(")").toString();
	}


	private boolean isMatchingLeafNode(String value, ExprNode exprNode) {
		return exprNode.isLeaf() && value.equals(((LeafNode)exprNode).getValue());
	}
}
