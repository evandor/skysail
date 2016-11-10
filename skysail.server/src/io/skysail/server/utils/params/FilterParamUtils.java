package io.skysail.server.utils.params;

import java.util.Set;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import io.skysail.server.domain.jvm.FieldFacet;
import io.skysail.server.domain.jvm.facets.NumberFacet;
import io.skysail.server.filter.ExprNode;
import io.skysail.server.filter.FilterParser;
import io.skysail.server.filter.FilterVisitor;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.utils.ParamsUtils;

public class FilterParamUtils extends ParamsUtils {

    public static final String FILTER_PARAM_KEY = SkysailServerResource.FILTER_PARAM_NAME;

    private FilterParser parser;

    public FilterParamUtils(String fieldname, Request request, FilterParser parser) {
        super(fieldname, request);
        this.parser = parser;
    }

    public String setMatchFilter(String value, FieldFacet facet) {
        return super.toggleLink(value, facet, null);
    }

    public String setMatchFilter(String value, FieldFacet facet, String format) {
        return super.toggleLink(value, facet, format);
    }

    public String reduceMatchFilter(String value, FieldFacet facet, String format) {
        return super.reduceLink(value, facet, format);
    }

    @Override
    protected Form handleQueryForm(FieldFacet facet, String format) {
        //return changeFilterQuery(fieldname, cloneForm(), getFilterParameter(), getValue(), format);
        Parameter found = getFilterParameter();// getOriginalForm().getFirst(FILTER_PARAM_KEY);
        if (found != null) {
            return changeFilterQuery(fieldname, cloneForm(), found, getValue(), format);
        }
        Form newForm = cloneForm();
        if (format == null) {
            format = "";
        }
        Form addFormParameters = facet.addFormParameters(newForm, fieldname, format, getValue());
        if (facet instanceof NumberFacet) {
//            int bucketId = Integer.parseInt(getValue());
//            List<Double> thresholds = ((NumberFacet)facet).getThresholds();
//            if (bucketId == 0) {
//                newForm.add(new Parameter(FILTER_PARAM_KEY, "(" + fieldname + format + "<" + thresholds.get(bucketId) + ")"));
//            } else if (bucketId == thresholds.size()) {
//                newForm.add(new Parameter(FILTER_PARAM_KEY, "(" + fieldname + format + ">" + thresholds.get(bucketId-1) + ")"));
//            } else {
//                newForm.add(new Parameter(FILTER_PARAM_KEY, "(&(" + fieldname + format + ">" + thresholds.get(bucketId-1) + ")(" + fieldname + format + "<" + thresholds.get(bucketId)+"))"));
//            }
//            return newForm;
            return addFormParameters;
        }


        //newForm.add(new Parameter(FILTER_PARAM_KEY, "(" + fieldname + format + "=" + getValue() + ")"));
        return newForm;

    }

    @Override
    protected Form reduceQueryForm(FieldFacet facet, String format) {
        Parameter found = getFilterParameter();// getOriginalForm().getFirst(FILTER_PARAM_KEY);
        if (found != null) {
            return reduceFilterQuery(fieldname, cloneForm(), found, facet, getValue(), format);
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

    private Form reduceFilterQuery(String fieldname, Form queryForm, Parameter found, FieldFacet facet, String value, String format) {
        queryForm.removeAll(FILTER_PARAM_KEY, true);
        if (parser != null) {

            String expr = facet.addFormParameters(new Form(), fieldname, format, value).getFirstValue(FILTER_PARAM_KEY);

            ExprNode filterNode = (ExprNode) parser.parse(found.getValue()).accept(new FilterVisitor() {
                @Override
                public Object visit(ExprNode node) {
                    return node.reduce(expr, facet, format);
                }
            });

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
