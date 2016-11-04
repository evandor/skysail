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

    private FilterParser parser;

    public FilterParamUtils(String fieldname, Request request, FilterParser parser) {
        super(fieldname, request);
        this.parser = parser;
    }

    private static final String FILTER_PARAM_KEY = SkysailServerResource.FILTER_PARAM_NAME;

    public String setMatchFilter(String value) {
        return super.toggleLink(value);
    }

    @Override
    protected void handleQueryForm() {
        Parameter found = getForm().getFirst(FILTER_PARAM_KEY);
        if (found != null) {
            changeFilterQuery(fieldname, getForm(), found, getValue());
            return;
        }
        getForm().add(new Parameter(FILTER_PARAM_KEY, "(" + fieldname + "=" + getValue() + ")"));
    }

    private Form changeFilterQuery(String fieldname, Form queryForm, Parameter found, String value) {
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
            if (filterKeys.contains(fieldname)) {
                queryForm.add(new Parameter(FILTER_PARAM_KEY, "(|" + found.getValue()+"("+fieldname+"="+value+"))"));
            } else {
                queryForm.add(new Parameter(FILTER_PARAM_KEY, "(&" + found.getValue()+"("+fieldname+"="+value+"))"));
            }
        }
        return queryForm;
    }

	public Parameter getFilterParameter() {
		return getForm().getFirst(FILTER_PARAM_KEY);
	}
}
