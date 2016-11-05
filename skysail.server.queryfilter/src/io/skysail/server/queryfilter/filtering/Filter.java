package io.skysail.server.queryfilter.filtering;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.restlet.Request;

import io.skysail.domain.Identifiable;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.filter.EntityEvaluationFilterVisitor;
import io.skysail.server.filter.PreparedStatement;
import io.skysail.server.filter.SqlFilterVisitor;
import io.skysail.server.queryfilter.parser.Parser;
import io.skysail.server.restlet.resources.SkysailServerResource;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@ToString(of = { "preparedStatement", "params" })
public class Filter {

    private String filterExpressionFromQuery;

    boolean valid = true;

    private String preparedStatement = "";

    private org.osgi.framework.Filter ldapFilter;

    private Map<String, Object> params = new HashMap<>();

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
            this.filterExpressionFromQuery = (String) filterQuery;
        } else if (defaultFilterExpression != null) {
            this.filterExpressionFromQuery = defaultFilterExpression;
        } else {
            // this.filterExpressionFromQuery = "1=1";
        }
        evaluate(facets);
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
            this.filterExpressionFromQuery = "(&" + filterExpressionFromQuery + filterExpression + ")";
        }
        evaluate(Collections.emptyMap());
    }

    public Filter add(String key, String value) {
        and("(" + key + "=" + value + ")");
        evaluate(Collections.emptyMap());
        return this;
    }

    public void addEdgeOut(String name, String value) {
        and("(" + value + " : out['" + name + "'] " + ")");
        evaluate(Collections.emptyMap());
    }

    public void addEdgeIn(String name, String value) {
        and("(" + value + " : in['" + name + "'] " + ")");
        evaluate(Collections.emptyMap());
    }

    public boolean match(String paramKey, Object gotten) {
        return false;
    }

    private void evaluate(Map<String, FieldFacet> facets) {
        if (filterExpressionFromQuery == null) {
            return;
        }
        try {
            String filter = getFilterFromQuery();
            Parser parser = new Parser();
            Object accept = parser.parse(filter).accept(new SqlFilterVisitor(facets));
            preparedStatement = ((PreparedStatement) accept).getSql();
            params = ((PreparedStatement) accept).getParams();
            return;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        valid = false;
    }

    public boolean evaluateEntity(Identifiable t, Map<String, FieldFacet> facets) {
        try {
            Parser parser = new Parser();
            return (boolean) parser.parse(getFilterFromQuery())
                    .accept(new EntityEvaluationFilterVisitor(t, facets));
        } catch (Exception e) {
           log.error(e.getMessage(), e);
        }
        return true;
    }

    private String getFilterFromQuery() throws UnsupportedEncodingException {
        return StringEscapeUtils.unescapeHtml4(filterExpressionFromQuery);
    }
}
