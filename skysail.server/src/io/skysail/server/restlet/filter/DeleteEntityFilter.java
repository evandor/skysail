package io.skysail.server.restlet.filter;

import org.slf4j.*;

import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.response.Wrapper;

public class DeleteEntityFilter<R extends EntityServerResource<T>, T extends Entity> extends AbstractResourceFilter<R, T> {

    private static Logger logger = LoggerFactory.getLogger(DeleteEntityFilter.class);

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        logger.debug("entering {}#doHandle", this.getClass().getSimpleName());
        resource.eraseEntity();
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

}
