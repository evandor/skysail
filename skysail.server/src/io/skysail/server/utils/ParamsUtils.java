package io.skysail.server.utils;

import java.util.Collections;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import io.skysail.server.domain.jvm.FieldFacet;
import lombok.Getter;
import lombok.ToString;

@ToString(of = "originalForm")
public abstract class ParamsUtils {

    @Getter
    private final String fieldname;

    private final Form originalForm;
    private final String hierarchicalPart;

    public ParamsUtils(String fieldname, Request request) {
        this.fieldname = fieldname;
        hierarchicalPart = request.getOriginalRef().getHierarchicalPart();
        this.originalForm = new Form(Collections.unmodifiableList(request.getOriginalRef().getQueryAsForm()));
    }

    protected abstract Form handleQueryForm(FieldFacet facet, String format, String value);

    protected abstract Form reduceQueryForm(FieldFacet facet, String format, String value);

    protected String toggleLink() {
        return this.toggleLink(null, null, null);
    }

    protected String toggleLink(String value, FieldFacet facet, String format) {
        Form form = handleQueryForm(facet, format, value);
        if (isEmpty(form)) {
            return emptyQueryRef();
        }
        form = stripEmptyParams(form);
        return isEmpty(form) ? hierarchicalPart : "?" + form.getQueryString();
    }

    protected String reduceLink(String value, FieldFacet facet, String format) {
        Form form = reduceQueryForm(facet, format, value);
        if (isEmpty(form)) {
            return emptyQueryRef();
        }
        form = stripEmptyParams(form);
        return isEmpty(form) ? "" : "?" + form.getQueryString();
    }

    protected String emptyQueryRef() {
        return "";//request.getOriginalRef().getHierarchicalPart();
    }

    protected boolean isEmpty(Form queryForm) {
        return "".equals(queryForm.getQueryString());
    }

    protected Form cloneForm() {
		Form result = new Form();
		originalForm.getValuesMap().keySet().forEach(key -> result.add(new Parameter(key, originalForm.getFirstValue(key))));
		return result;
	}

	protected Parameter getParameter(String filterParamKey) {
		return originalForm.getFirst(filterParamKey);
	}

    private Form stripEmptyParams(Form form) {
        Form result = new Form();
        form.forEach(param -> {
            if (param.getValue() != null && !"".equals(param.getValue().trim())) {
                result.add(param);
            }
        });
        return result;
    }

}
