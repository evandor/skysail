package io.skysail.server.queryfilter;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.restlet.Request;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.queryfilter.parser.Parser;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ToString(of = {"preparedStatement", "params"})
public class Filter {

    private String filterExpressionFromQuery;
    boolean valid = true;
    private Long filterId;
    private String preparedStatement = "";
    private org.osgi.framework.Filter ldapFilter;
    private Map<String, Object> params = new HashMap<>();

    public Filter() {
        this((Request)null, null, null);
    }

    public Filter(Request request) {
        this(request, null);
    }

    public Filter(Request request, Map<String, FieldFacet> facets) {
        this(request, facets, null);
    }

    public Filter(Request request, Map<String, FieldFacet> facets, String defaultFilterExpression) {
        if (request == null) {
            return;
        }
        Object filterQuery = request.getAttributes().get(SkysailServerResource.FILTER_PARAM_NAME);
        if (filterQuery != null) {
            this.filterExpressionFromQuery = (String)filterQuery;
        } else if (defaultFilterExpression != null) {
            this.filterExpressionFromQuery = defaultFilterExpression;
        } else {
            //this.filterExpressionFromQuery = "1=1";
        }
        evaluate(facets);
    }

    public Filter(String key, String value) {
        this("(" + key + "=" + value +")");
    }

    public Filter(String filterExpression) {
        this.filterExpressionFromQuery = filterExpression;
        evaluate(Collections.emptyMap());
    }

    public void and(String filterExpression) {
        if (filterExpression == null || filterExpression.trim().length() == 0) {
            return;
        }
        if (filterExpressionFromQuery == null) {
            this.filterExpressionFromQuery = filterExpression;
        } else {
            this.filterExpressionFromQuery = "(&"+filterExpressionFromQuery+filterExpression+")";
        }
        evaluate(Collections.emptyMap());
    }

    public Filter add(String key, String value) {
        and("(" + key + "=" + value +")");
        evaluate(Collections.emptyMap());
        return this;
    }

    public void addEdgeOut(String name, String value) {
        and("("+value+" : out['"+name+"'] " +")");
        evaluate(Collections.emptyMap());
    }

    public void addEdgeIn(String name, String value) {
        and("("+value+" : in['"+name+"'] " +")");
        evaluate(Collections.emptyMap());
    }
    
    public boolean match(String paramKey, Object gotten) {
		// TODO Auto-generated method stub
		return false;
	}

    private void evaluate(Map<String, FieldFacet> facets) {
        if (filterExpressionFromQuery == null) {
            return;
        }
        try {
        	String filter = getFilterFromQuery();
            Parser parser = new Parser(filter);
            Object accept = parser.parse().accept(new SqlFilterVisitor(facets));
            preparedStatement = ((PreparedStatement) accept).getSql();
            params = ((PreparedStatement)accept).getParams();
            return;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        valid = false;
    }

	public boolean evaluateEntity(Identifiable t, Class<? extends Identifiable> cls, Map<String, FieldFacet> facets) {
		Map<String, Method> getters = new HashMap<>();
		Set<String> paramKeys = getParams().keySet();
		for (String paramKey : paramKeys) {
			try {
				String getterName = "get" + paramKey.substring(0, 1).toUpperCase() + paramKey.substring(1);
				Method getter = cls.getDeclaredMethod(getterName);
				getters.put(paramKey, getter);
			} catch (NoSuchMethodException | SecurityException e) {
				log.error(e.getMessage(),e);
			}
		}
		try {
			Parser parser = new Parser(getFilterFromQuery());
	        Object accept = parser.parse().accept(new EntityEvaluationVisitor(facets));
	        System.out.println(accept);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		for (String paramKey : filter.getParams().keySet()) {
//			try {
//				Method getter = getters.get(paramKey);
//				if (getter == null) {
//					continue;
//				}
//				Object gotten = getter.invoke(t);
//				//if (!filter.getParams().get(paramKey).equals(gotten)) {
//				if (!filter.match(paramKey, gotten)) {
//					return false;
//				}
//			} catch (Exception e) {
//				log.error(e.getMessage(),e);
//			}
//		}
//		return true;
		return true;
	}
	
	private String getFilterFromQuery() throws UnsupportedEncodingException {
		String filter;
        filter = java.net.URLDecoder.decode(filterExpressionFromQuery, "UTF-8");
        filter = StringEscapeUtils.unescapeHtml4(filterExpressionFromQuery);
        //filterId = Long.parseLong(filter); // TODO remove filter id logic
		return filter;
	}
}
