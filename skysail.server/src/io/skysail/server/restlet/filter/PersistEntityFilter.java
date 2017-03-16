package io.skysail.server.restlet.filter;

import org.restlet.Response;

import io.skysail.core.app.SkysailApplication;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.restlet.response.Wrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersistEntityFilter<R extends SkysailServerResource<T>, T extends Entity> extends
        AbstractResourceFilter<R, T> {

    public PersistEntityFilter(SkysailApplication skysailApplication) {
    }

    @SuppressWarnings("unchecked")
    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        Response response = responseWrapper.getResponse();
        Object entity = responseWrapper.getEntity();
        ((PostEntityServerResource<T>) resource).addEntity((T)entity);
        String id = ((T)entity).getId();
        if (id != null) {
            response.setLocationRef(response.getRequest().getResourceRef().addSegment(id.replace("#", "")));
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
