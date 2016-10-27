package io.skysail.server.utils.params;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ParamsUtils;

public class FilterParamUtils extends ParamsUtils {

    public FilterParamUtils(String fieldname, Request request) {
		super(fieldname, request);
	}

	private static final String FILTER_PARAM_KEY = SkysailServerResource.FILTER_PARAM_NAME;

	public String setMatchFilter(String value) {
		return super.toggleLink(request, fieldname);
	}

	@Override
	protected Form handleQueryForm() {
		Form queryForm = request.getOriginalRef().getQueryAsForm();
        Parameter found = queryForm.getFirst(FILTER_PARAM_KEY);
        if (found != null) {
            return changeFilterQuery(fieldname, queryForm, found, getValue());
        }
        queryForm.add(new Parameter(FILTER_PARAM_KEY, "(" + fieldname + "=" + getValue()+")"));
        return queryForm;
    }

	private Form changeFilterQuery(String fieldname, Form queryForm, Parameter found, String value) {
		 queryForm.removeAll(FILTER_PARAM_KEY, true);
	        queryForm.add(new Parameter(FILTER_PARAM_KEY, "("+fieldname+"=)"));
	        return queryForm;
	}

    private String newMatchQuery(String fieldname, String value) {
        Form queryForm = new Form();
        queryForm.add(new Parameter(FILTER_PARAM_KEY, "(" + fieldname + "=" + value +")"));
        return "?" + queryForm.getQueryString();
    }
}
