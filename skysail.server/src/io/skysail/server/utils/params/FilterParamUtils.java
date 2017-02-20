package io.skysail.server.utils.params;

import java.util.Set;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.FilterParser;
import io.skysail.server.filter.FilterVisitor;
import io.skysail.server.utils.ParamsUtils;

public final class FilterParamUtils extends ParamsUtils {

    public static final String FILTER_PARAM_KEY = SkysailServerResource.FILTER_PARAM_NAME;

    private final FilterParser parser;

    public FilterParamUtils(String fieldname, Request request, FilterParser parser) {
        super(fieldname, request);
        this.parser = parser;
    }

    public String setMatchFilter(String bucketId, FieldFacet facet) {
        return super.toggleLink(bucketId, facet, null);
    }

    public String setMatchFilter(String value, FieldFacet facet, String format) {
        return super.toggleLink(value, facet, format);
    }

    public String reduceMatchFilter(String value, FieldFacet facet, String format) {
        return super.reduceLink(value, facet, format);
    }

    @Override
    protected Form handleQueryForm(FieldFacet facet, String format, String value) {
        Form addFormParameters = facet.addFormParameters(new Form(), getFieldname(), handleFormat(format), value);
        if (getFilterParameter() != null && !"".equals(getFilterParameter().getValue())) {
            return changeFilterQuery(getFieldname(), cloneForm(), getFilterParameter(), value, handleFormat(format),
                    addFormParameters.getFirstValue("_f"));
        }
        return addFormParameters;
    }

    @Override
    protected Form reduceQueryForm(FieldFacet facet, String format, String value) {
        Form addFormParameters = facet.addFormParameters(new Form(), getFieldname(), handleFormat(format), value);
        if (getFilterParameter() != null && !"".equals(getFilterParameter().getValue())) {
            return reduceFilterQuery(getFieldname(), cloneForm(), getFilterParameter(), value, facet, handleFormat(format),addFormParameters.getFirstValue("_f"));
        }
//        Form newForm = cloneForm();
//        if (format == null) {
//            format = "";
//        }
//        newForm.add(new Parameter(FILTER_PARAM_KEY, "(" + getFieldname() + format + "=" + value + ")"));
        return addFormParameters;
    }

    public Parameter getFilterParameter() {
        Parameter filterParam = getParameter(FILTER_PARAM_KEY);
        if (filterParam == null || "".equals(filterParam.getValue())) {
            return null;
        }
        return filterParam;
    }

    private Form changeFilterQuery(String fieldname, Form queryForm, Parameter found, String value, String format,
            String paramValue) {
        queryForm.removeAll(FILTER_PARAM_KEY, true);
        if (parser != null) {
            @SuppressWarnings("unchecked")
            Set<String> filterKeys = (Set<String>) parser.parse(found.getValue()).accept(new FilterVisitor() {
                @Override
                public Object visit(ExprNode node) {
                    return node.getKeys();
                }
            });
            if (filterKeys.contains(fieldname)) {
                if (found.getValue().equals(paramValue)) {
                    queryForm.add(new Parameter(FILTER_PARAM_KEY,
                            found.getValue()));
                } else {
                    queryForm.add(new Parameter(FILTER_PARAM_KEY,
                            "(|" + found.getValue() + paramValue + ")"));
                }
            } else {
                queryForm.add(new Parameter(FILTER_PARAM_KEY,
                        "(&" + found.getValue() + paramValue + ")"));
            }
        }
        return queryForm;
    }

    /**
     * @param fieldname a
     * @param queryForm [[_f=(|(a<0.0)(&(a>0.0)(a<100.0)))]]
     * @param found     [[_f=(|(a<0.0)(&(a>0.0)(a<100.0)))]]
     * @param value     1
     * @param facet     NumberFacet (0,100)
     * @param format    ""
     * @param paramValue (&(a>0.0)(a<100.0))
     * @return
     */
    private Form reduceFilterQuery(String fieldname, Form queryForm, Parameter found, String value, FieldFacet facet,
            String format, String paramValue) {
        queryForm.removeAll(FILTER_PARAM_KEY, true);
        if (parser != null) {

            //String expr = facet.addFormParameters(new Form(), fieldname, format, value).getFirstValue(FILTER_PARAM_KEY);

            ExprNode filterNode = (ExprNode) parser.parse(found.getValue()).accept(new FilterVisitor() {
                @Override
                public Object visit(ExprNode node) {
                    return node.reduce(paramValue, facet, format);
                }
            });

            String filterString = (String) filterNode.accept(new FilterVisitor() {

                @Override
                public String visit(ExprNode node) {
                    return node.asLdapString();
                }
            });
            String asLdapString = filterNode.asLdapString();

            queryForm.add(new Parameter(FILTER_PARAM_KEY, filterString));
        }
        return queryForm;
    }

    private String handleFormat(String format) {
        return (format == null || format.trim().length() == 0) ? "" : ";"+format;
    }


}
