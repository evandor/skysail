package io.skysail.server.restlet.filter;

import org.restlet.Response;

import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.restlet.response.Wrapper;

public class DeleteListRedirectGetFilter<R extends ListServerResource<T>, T extends Entity> extends AbstractResourceFilter<R, T> {

    @Override
    protected void afterHandle(R resource, Wrapper<T> responseWrapper) {
        Response response = responseWrapper.getResponse();
        String redirectTo = resource.redirectTo();
        if (redirectTo != null) {
            response.redirectSeeOther(redirectTo);
        }
    }
}
