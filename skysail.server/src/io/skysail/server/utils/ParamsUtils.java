package io.skysail.server.utils;

import org.restlet.Request;
import org.restlet.data.Form;

public abstract class ParamsUtils {


    protected String emptyQueryRef(Request request) {
        return request.getOriginalRef().getHierarchicalPart();
    }

    protected boolean isEmpty(Form queryForm) {
        return "".equals(queryForm.getQueryString());
    }

}
