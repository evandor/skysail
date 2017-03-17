package io.skysail.core.requests;

import org.restlet.resource.ResourceException;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.server.restlet.filter.FilterResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionCatchingFilter extends AbstractResourceFilter {

    @Override
    public FilterResult doHandle(SkysailServerResource<?> resource, Wrapper2 responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        try {
            super.doHandle(resource, responseWrapper);
        } catch (ResourceException re) {
            throw re;
        } catch (Exception e) {
            ExceptionCatchingFilterHelper.handleError(e, resource.getApplication(), responseWrapper, resource.getClass());
        }
        return FilterResult.CONTINUE;
    }

    @Override
    protected void afterHandle(SkysailServerResource<?> resource, Wrapper2 responseWrapper) {
    	resource.getServerInfo().setAgent("Skysail-Server/0.0.1 " + resource.getServerInfo().getAgent());
    }
}
