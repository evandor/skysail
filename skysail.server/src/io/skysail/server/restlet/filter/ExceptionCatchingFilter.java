package io.skysail.server.restlet.filter;

import org.restlet.resource.ResourceException;

import io.skysail.core.app.SkysailApplication;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.filter.helper.ExceptionCatchingFilterHelper;
import io.skysail.server.restlet.response.Wrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionCatchingFilter<R extends SkysailServerResource<?>, T extends Entity> extends
        AbstractResourceFilter<R, T> {

    private SkysailApplication application;

    public ExceptionCatchingFilter(SkysailApplication application) {
        this.application = application;
    }

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        try {
            super.doHandle(resource, responseWrapper);
        } catch (ResourceException re) {
            throw re;
        } catch (Exception e) {
            ExceptionCatchingFilterHelper.handleError(e, application, responseWrapper, resource.getClass());
        }
        return FilterResult.CONTINUE;
    }

    @Override
    protected void afterHandle(R resource, Wrapper<T> responseWrapper) {
    	resource.getServerInfo().setAgent("Skysail-Server/0.0.1 " + resource.getServerInfo().getAgent());
    }
}
