package io.skysail.server.restlet.filter;

import org.restlet.Response;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.response.Wrapper;
import io.skysail.server.restlet.response.Wrapper2;
import io.skysail.server.utils.HeadersUtils;

public class SetExecutionTimeInListResponseFilter extends AbstractListResourceFilter {

    @Override
    protected void afterHandle(SkysailServerResource<?> resource, Wrapper2 responseWrapper) {
        Response response = responseWrapper.getResponse();
        if (response.getRequest().getAttributes() == null) {
            return;
        }
        Long startTime = (Long) response.getRequest().getAttributes().get("org.restlet.startTime");
        if (startTime == null) {
            return;
        }
        Long duration = System.currentTimeMillis() - startTime;
        HeadersUtils.addToHeaders(response, "X-Duration", duration.toString());
    }
}
