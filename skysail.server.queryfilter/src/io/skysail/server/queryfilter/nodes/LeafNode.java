package io.skysail.server.queryfilter.nodes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.Operation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class LeafNode extends AbstractExprNode {

    protected String attribute;
    private String value;

    protected LeafNode(Operation op) {
        super(op);
    }

    protected LeafNode(String attribute, Operation op) {
        super(op);
        this.attribute = attribute;
    }

    public LeafNode(Operation op, String attribute, String value) {
        super(op);
        this.attribute = attribute;
        this.value = value != null ? value.trim() : "";
    }

    @Override
    public final boolean isLeaf() {
        return true;
    }

    @Override
    public Set<String> getSelected() {
        return Collections.emptySet();
    }


    @Override
    public Set<String> getKeys() {
        Set<String> result = new HashSet<>();
        String attributeWithoutFormat = getAttribute().split(";",2)[0];
        result.add(attributeWithoutFormat);
        return result;
    }
    
    @Override
	public ExprNode reduce(String value, String format) {
		return this;
	}


}
