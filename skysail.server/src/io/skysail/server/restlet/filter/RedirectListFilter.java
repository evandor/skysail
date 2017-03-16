package io.skysail.server.restlet.filter;

import org.restlet.Response;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.server.restlet.response.Wrapper2;

public class RedirectListFilter extends AbstractListResourceFilter  {

    @Override
    protected void afterHandle(SkysailServerResource<?> resource, Wrapper2 responseWrapper) {
        String redirectTo = resource.redirectTo();
        if (redirectTo != null) {
            Response response = responseWrapper.getResponse();
            response.redirectSeeOther(redirectTo);
        }
    }
}
