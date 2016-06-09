package io.skysail.server.um.shiro.web.impl;

import java.io.Serializable;

import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.restlet.Request;
import org.restlet.Response;

public class RestletSessionKey extends DefaultSessionKey implements RestletRequestPairSource {

    private final transient Request request;
    private final transient Response response;

    public RestletSessionKey(Request request, Response response) {
        if (request == null) {
            throw new NullPointerException("request argument cannot be null.");
        }
        if (response == null) {
            throw new NullPointerException("response argument cannot be null.");
        }
        this.request = request;
        this.response = response;
    }

    public RestletSessionKey(Serializable sessionId, Request request, Response response) {
        this(request, response);
        setSessionId(sessionId);
    }

    @Override
    public Request getRestletRequest() {
        return request;
    }

    @Override
    public Response getRestletResponse() {
        return response;
    }
}
