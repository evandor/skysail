package io.skysail.server.um.shiro.web;

import org.apache.shiro.session.mgt.SessionContext;
import org.restlet.Request;
import org.restlet.Response;

import io.skysail.server.um.shiro.web.impl.RestletRequestPairSource;

public interface RestletSessionContext extends SessionContext, RestletRequestPairSource {

    @Override
    Request getRestletRequest();

    void setRequest(Request request);

    @Override
    Response getRestletResponse();

    void setResponse(Response response);
}
