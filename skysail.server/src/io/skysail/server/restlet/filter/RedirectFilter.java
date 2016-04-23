package io.skysail.server.restlet.filter;

import org.restlet.Response;

import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.response.Wrapper;

public class RedirectFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, Wrapper<T> responseWrapper) {
        String redirectTo = resource.redirectTo();
        if (redirectTo != null) {
            Response response = responseWrapper.getResponse();
            response.redirectSeeOther(redirectTo);
        }
    }
}
