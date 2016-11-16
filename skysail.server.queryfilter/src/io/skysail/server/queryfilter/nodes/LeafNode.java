package io.skysail.server.queryfilter.nodes;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.filter.EntityEvaluationFilterVisitor;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.Operation;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
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

    protected abstract boolean handleFacet(String attributeName, String format, Map<String, FieldFacet> facets, Object gotten);

    @Override
    public final boolean isLeaf() {
        return true;
    }

    @Override
    public Set<String> getSelected(FieldFacet facet,Map<String, String> lines) {
        //return Collections.emptySet();
        Set<String> result = new HashSet<>();

        lines.keySet().forEach(key -> {
            if (lines.get(key).equals(asLdapString())) {
                result.add(key);
            }
        });
        return result;
    }


    @Override
    public Set<String> getKeys() {
        Set<String> result = new HashSet<>();
        String attributeWithoutFormat = getAttribute().split(";",2)[0];
        result.add(attributeWithoutFormat);
        return result;
    }

    @Override
	public ExprNode reduce(String value, FieldFacet facet,String format) {
		return this;
	}

    @Override
    public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor) {
        String attributeName;
        String format = null;
        int semicolonPosition = getAttribute().indexOf(";");
        if (semicolonPosition < 0) {
            attributeName = getAttribute();
        } else {
            attributeName = getAttribute().substring(0, semicolonPosition);
            format = getAttribute().substring(semicolonPosition + 1);
        }
        Identifiable t = entityEvaluationVisitor.getT();
        Map<String, FieldFacet> facets = entityEvaluationVisitor.getFacets();
        try {
            String getterName = "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
            Method getter = t.getClass().getDeclaredMethod(getterName);
            Object gotten = getter.invoke(t);
            if (facets.containsKey(attributeName)) {
                return handleFacet(attributeName, format, facets, gotten);
            } else {
                return getValue().equals(gotten);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean evaluateValue(Object gotten) {
        // TODO Auto-generated method stub
        return false;
    }


}
