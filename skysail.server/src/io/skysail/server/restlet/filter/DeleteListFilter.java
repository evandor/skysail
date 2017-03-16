package io.skysail.server.restlet.filter;

import java.util.List;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.response.Wrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeleteListFilter<R extends SkysailServerResource<List<T>>, T extends Entity> extends
    AbstractListResourceFilter<R, T> {

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        resource.eraseEntity();
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

}
