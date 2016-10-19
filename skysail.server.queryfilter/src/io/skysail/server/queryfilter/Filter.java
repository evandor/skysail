package io.skysail.server.queryfilter;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.restlet.Request;

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
    private Map<String, Object> params;
    //private Map<String, FieldFacet> facets;

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

    private void evaluate(Map<String, FieldFacet> facets) {
        if (filterExpressionFromQuery == null) {
            return;
        }
        String filter;
        try {
            filter = java.net.URLDecoder.decode(filterExpressionFromQuery, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return;
        }
        filter = StringEscapeUtils.unescapeHtml4(filterExpressionFromQuery);
        try {
            filterId = Long.parseLong(filter); // TODO remove filter id logic
            return;
        } catch (Exception e) {
            // that's ok
        }
        try {
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
}
