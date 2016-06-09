package io.skysail.server.um.shiro.web.impl;

import java.util.Map;

import org.apache.shiro.session.mgt.DefaultSessionContext;
import org.apache.shiro.web.session.mgt.DefaultWebSessionContext;
import org.restlet.Request;
import org.restlet.Response;

import io.skysail.server.um.shiro.web.RestletSessionContext;

public class SkysailWebSessionContext extends DefaultSessionContext implements RestletSessionContext {

    private static final String RESTLET_REQUEST = DefaultWebSessionContext.class.getName() + ".RESTLET_REQUEST";
    private static final String RESTLET_RESPONSE = DefaultWebSessionContext.class.getName() + ".RESTLET_RESPONSE";

    public SkysailWebSessionContext() {
        super();
    }

    public SkysailWebSessionContext(Map<String, Object> map) {
        super(map);
    }

    @Override
    public void setRequest(Request request) {
        if (request != null) {
            put(RESTLET_REQUEST, request);
        }
    }

    @Override
    public Request getRestletRequest() {
        return getTypedValue(RESTLET_REQUEST, Request.class);
    }

    @Override
    public void setResponse(Response response) {
        if (response != null) {
            put(RESTLET_RESPONSE, response);
        }
    }

    @Override
    public Response getRestletResponse() {
        return getTypedValue(RESTLET_RESPONSE, Response.class);
    }

}
