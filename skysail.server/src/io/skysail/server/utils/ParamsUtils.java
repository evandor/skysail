package io.skysail.server.utils;

import java.util.Collections;

import org.restlet.Request;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

import lombok.Getter;
import lombok.ToString;

@ToString(of = "originalForm")
public abstract class ParamsUtils {

    @Getter
    private String value;

    private final Form originalForm;

    protected String fieldname;
    protected Request request;

    public ParamsUtils(String fieldname, Request request) {
        this.fieldname = fieldname;
        this.request = request;
        this.originalForm = new Form(Collections.unmodifiableList(request.getOriginalRef().getQueryAsForm()));
    }

    protected abstract Form handleQueryForm(String format);

    protected abstract Form reduceQueryForm(String format);

    protected String toggleLink() {
        return this.toggleLink(null, null);
    }

    protected String toggleLink(String value, String format) {
        this.value = value;
        Form form = handleQueryForm(format);
        if (isEmpty(form)) {
            return emptyQueryRef(request);
        }
        form = stripEmptyParams(form);
        return isEmpty(form) ? request.getOriginalRef().getHierarchicalPart() : "?" + form.getQueryString();
    }

    protected String reduceLink(String value, String format) {
        this.value = value;
        Form form = reduceQueryForm(format);
        if (isEmpty(form)) {
            return emptyQueryRef(request);
        }
        form = stripEmptyParams(form);
        return isEmpty(form) ? "" : "?" + form.getQueryString();
    }

    protected String emptyQueryRef(Request request) {
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

    private Form cloneForm(Form originalForm) {
        Form form = new Form();
        originalForm.getValuesMap().keySet().stream().forEach(k -> form.add(new Parameter(k,originalForm.getFirstValue(k))));
        return form;
    }

}
