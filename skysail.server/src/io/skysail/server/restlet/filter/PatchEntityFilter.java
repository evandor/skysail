package io.skysail.server.restlet.filter;

import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.PatchEntityServerResource;
import io.skysail.server.restlet.response.Wrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PatchEntityFilter<R extends PatchEntityServerResource<T>, T extends Identifiable>
        extends AbstractResourceFilter<R, T> {

    @SuppressWarnings("unchecked")
    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        Object entity = responseWrapper.getEntity();
        if (entity != null) {
            resource.updateEntity((T) entity);
            //SkysailResponse<T> response = new SkysailResponse<>();
            // responseWrapper.setEntity((T)(response.getEntity()));
            // resource.setCurrentEntity(response.getEntity());
        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }

}
