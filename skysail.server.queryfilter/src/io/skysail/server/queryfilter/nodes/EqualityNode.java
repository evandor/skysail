package io.skysail.server.queryfilter.nodes;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.queryfilter.EntityEvaluationFilterVisitor;
import io.skysail.server.queryfilter.Operation;
import io.skysail.server.queryfilter.PreparedStatement;
import io.skysail.server.queryfilter.SqlFilterVisitor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString(callSuper = true)
@Slf4j
public class EqualityNode extends LeafNode {

    public EqualityNode(String attribute, String value) {
        super(Operation.EQUAL, attribute, value);
    }

    @Override
    public PreparedStatement createPreparedStatement(SqlFilterVisitor sqlFilterVisitor,
            Map<String, FieldFacet> facets) {
        PreparedStatement ps = new PreparedStatement();
        String attributeName = getAttribute();
        if (facets.containsKey(attributeName)) {
            ps.append(facets.get(attributeName).sqlFilterExpression(getValue()));
            ps.put(attributeName, getValue());
        } else {
            ps.append(attributeName).append("=:").append(attributeName);
            ps.put(attributeName, getValue());
        }
        return ps;
    }

	@Override
	public boolean evaluateEntity(EntityEvaluationFilterVisitor entityEvaluationVisitor, Identifiable t,  Map<String, FieldFacet> facets) {
	    String attributeName;
	    String format = null;
	    int semicolonPosition = getAttribute().indexOf(";");
        if (semicolonPosition < 0 ) {
            attributeName = getAttribute();
	    } else {
	        attributeName = getAttribute().substring(0, semicolonPosition);
            format = getAttribute().substring(semicolonPosition+1);
	    }
	    try {
            String getterName = "get" + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1);
            Method getter = t.getClass().getDeclaredMethod(getterName);
            Object gotten = getter.invoke(t);
            if (facets.containsKey(attributeName)) {
                FieldFacet fieldFacet = facets.get(attributeName);
                String sqlFilterExpression = fieldFacet.sqlFilterExpression(gotten.toString());
                if (format == null || "".equals(format.trim())) {
                    return false;
                }

                SimpleDateFormat sdf = new SimpleDateFormat(format);
                return sdf.format((Date)gotten).equals(getValue());
            } else {
                return getValue().equals(gotten);
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
		return false;
	}

}
