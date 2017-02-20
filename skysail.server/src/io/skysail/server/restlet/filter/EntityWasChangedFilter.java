package io.skysail.server.restlet.filter;

import io.skysail.core.app.SkysailApplication;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.response.Wrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityWasChangedFilter<R extends SkysailServerResource<T>, T extends Entity> extends AbstractResourceFilter<R, T> {

    public EntityWasChangedFilter(SkysailApplication skysailApplication) {
    }

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        String infoMessage = resource.getClass().getSimpleName() + ".changed.success";
        responseWrapper.addInfo(infoMessage);
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
