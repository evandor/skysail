package io.skysail.server.restlet.filter;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.server.restlet.response.Wrapper2;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeleteListFilter extends AbstractListResourceFilter {

    @Override
    public FilterResult doHandle(SkysailServerResource<?> resource, Wrapper2 responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        resource.eraseEntity();
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

}
