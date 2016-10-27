package io.skysail.server.utils.params;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;
import org.restlet.data.Reference;

import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ParamsUtils;

public class FilterParamUtils extends ParamsUtils {

    private static final String FILTER_PARAM_KEY = SkysailServerResource.FILTER_PARAM_NAME;

	public String setMatchFilter(Request request, String fieldname, String value) {
		Reference originalRef = request.getOriginalRef();
        if (!originalRef.hasQuery()) {
            return newMatchQuery(fieldname, value);
        }

        Form queryForm = handleFilterQueryForm(fieldname, originalRef.getQueryAsForm(), value);
        if (isEmpty(queryForm)) {
            return emptyQueryRef(request);
        }
        queryForm = stripEmptyParams(queryForm);
        return isEmpty(queryForm) ? request.getOriginalRef().getHierarchicalPart() : "?" + queryForm.getQueryString();
	}

    private static Form handleFilterQueryForm(String fieldname, Form queryForm, String value) {
        Parameter found = queryForm.getFirst(FILTER_PARAM_KEY);
        if (found != null) {
            return changeFilterQuery(fieldname, queryForm, found, value);
        }
        queryForm.add(new Parameter(FILTER_PARAM_KEY, "(" + fieldname + "=" + value +")"));
        return queryForm;
    }

	private static Form changeFilterQuery(String fieldname, Form queryForm, Parameter found, String value) {
		 queryForm.removeAll(FILTER_PARAM_KEY, true);
	        queryForm.add(new Parameter(FILTER_PARAM_KEY, "("+fieldname+"=)"));
	        return queryForm;
	}

    private static String newMatchQuery(String fieldname, String value) {
        Form queryForm = new Form();
        queryForm.add(new Parameter(FILTER_PARAM_KEY, "(" + fieldname + "=" + value +")"));
        return "?" + queryForm.getQueryString();
    }

    private static Form stripEmptyParams(Form queryForm) {
        Form result = new Form();
        queryForm.forEach(param -> {
            if (param.getValue() != null && !"".equals(param.getValue().trim())) {
                result.add(param);
            }
        });
        return result;
    }






}
