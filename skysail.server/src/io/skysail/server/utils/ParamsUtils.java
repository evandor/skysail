package io.skysail.server.utils;

import org.restlet.Request;
import org.restlet.data.Form;

import lombok.Getter;
import lombok.ToString;

@ToString(of = "form")
public abstract class ParamsUtils {

    @Getter
    private String value;

    @Getter
    private Form form;

    protected String fieldname;
    protected Request request;

    public ParamsUtils(String fieldname, Request request) {
        this.fieldname = fieldname;
        this.request = request;
        this.form = request.getOriginalRef().getQueryAsForm();
    }

    protected abstract void handleQueryForm();

    protected String toggleLink() {
        return this.toggleLink(null);
    }

    protected String toggleLink(String value) {
        this.value = value;
        handleQueryForm();
        if (isEmpty(getForm())) {
            return emptyQueryRef(request);
        }
        stripEmptyParams();
        return isEmpty(getForm()) ? request.getOriginalRef().getHierarchicalPart() : "?" + getForm().getQueryString();
    }

    protected String emptyQueryRef(Request request) {
        return request.getOriginalRef().getHierarchicalPart();
    }

    protected boolean isEmpty(Form queryForm) {
        return "".equals(queryForm.getQueryString());
    }

    private void stripEmptyParams() {
        Form result = new Form();
        getForm().forEach(param -> {
            if (param.getValue() != null && !"".equals(param.getValue().trim())) {
                result.add(param);
            }
        });
        this.form = result;
    }
}
