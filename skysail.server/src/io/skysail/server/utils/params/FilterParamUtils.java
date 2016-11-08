package io.skysail.server.utils.params;

import java.util.Set;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.FilterParser;
import io.skysail.server.filter.FilterVisitor;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ParamsUtils;

public class FilterParamUtils extends ParamsUtils {

    private static final String FILTER_PARAM_KEY = SkysailServerResource.FILTER_PARAM_NAME;

    private FilterParser parser;

    public FilterParamUtils(String fieldname, Request request, FilterParser parser) {
        super(fieldname, request);
        this.parser = parser;
    }

    public String setMatchFilter(String value) {
        return super.toggleLink(value, null);
    }

    public String setMatchFilter(String value, String format) {
        return super.toggleLink(value, format);
    }

    public String reduceMatchFilter(String value, String format) {
        return super.reduceLink(value, format);
    }

    @Override
    protected Form handleQueryForm(String format) {
        Parameter found = getFilterParameter();// getOriginalForm().getFirst(FILTER_PARAM_KEY);
        if (found != null) {
            return changeFilterQuery(fieldname, cloneForm(), found, getValue(), format);
        }
        Form newForm = cloneForm();
        if (format == null) {
            format = "";
        }
        newForm.add(new Parameter(FILTER_PARAM_KEY, "(" + fieldname + format + "=" + getValue() + ")"));
        return newForm;
    }

    @Override
    protected Form reduceQueryForm(String format) {
        Parameter found = getFilterParameter();// getOriginalForm().getFirst(FILTER_PARAM_KEY);
        if (found != null) {
            return reduceFilterQuery(fieldname, cloneForm(), found, getValue(), format);
        }
        Form newForm = cloneForm();
        if (format == null) {
            format = "";
        }
        newForm.add(new Parameter(FILTER_PARAM_KEY, "(" + fieldname + format + "=" + getValue() + ")"));
        return newForm;
    }

    public Parameter getFilterParameter() {
        return getParameter(FILTER_PARAM_KEY);
    }

    private Form changeFilterQuery(String fieldname, Form queryForm, Parameter found, String value, String format) {
        queryForm.removeAll(FILTER_PARAM_KEY, true);
        if (parser != null) {
            @SuppressWarnings("unchecked")
            Set<String> filterKeys = (Set<String>) parser.parse(found.getValue()).accept(new FilterVisitor() {
                @Override
                public Object visit(ExprNode node) {
                    return node.getKeys();
                }
            });
            System.out.println(filterKeys);
            if (format == null) {
                format = "";
            }

            if (filterKeys.contains(fieldname)) {
                queryForm.add(new Parameter(FILTER_PARAM_KEY,
                        "(|" + found.getValue() + "(" + fieldname + format + "=" + value + "))"));
            } else {
                queryForm.add(new Parameter(FILTER_PARAM_KEY,
                        "(&" + found.getValue() + "(" + fieldname + format + "=" + value + "))"));
            }
        }
        return queryForm;
    }

    private Form reduceFilterQuery(String fieldname, Form queryForm, Parameter found, String value, String format) {
        queryForm.removeAll(FILTER_PARAM_KEY, true);
        if (parser != null) {
            @SuppressWarnings("unchecked")
            ExprNode filterNode = (ExprNode) parser.parse(found.getValue()).accept(new FilterVisitor() {
                @Override
                public Object visit(ExprNode node) {
                    return node.reduce(value, format);
                }
            });
            System.out.println(filterNode);

            if (filterNode == null) {
                return queryForm;
            }

            String filterString = (String) filterNode.accept(new FilterVisitor() {

                @Override
                public String visit(ExprNode node) {
                    return node.render();
                }
            });
            queryForm.add(new Parameter(FILTER_PARAM_KEY, filterString));
        }
        return queryForm;
    }

}
