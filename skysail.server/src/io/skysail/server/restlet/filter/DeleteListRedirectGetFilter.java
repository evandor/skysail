package io.skysail.server.restlet.filter;

import java.util.List;

import org.restlet.Response;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.response.Wrapper;

public class DeleteListRedirectGetFilter<R extends SkysailServerResource<List<T>>, T extends Entity> extends AbstractListResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, Wrapper<T> responseWrapper) {
        Response response = responseWrapper.getResponse();
        String redirectTo = resource.redirectTo();
        if (redirectTo != null) {
            response.redirectSeeOther(redirectTo);
        }
    }
}
