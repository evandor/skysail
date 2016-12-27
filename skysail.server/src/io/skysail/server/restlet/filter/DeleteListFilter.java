package io.skysail.server.restlet.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.restlet.response.Wrapper;

public class DeleteListFilter<R extends ListServerResource<T>, T extends Identifiable> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(DeleteListFilter.class);

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        resource.eraseEntity();
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

}
