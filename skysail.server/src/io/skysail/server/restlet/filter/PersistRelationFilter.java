package io.skysail.server.restlet.filter;

import java.util.List;

import io.skysail.core.app.SkysailApplication;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.PostRelationResource;
import io.skysail.server.restlet.response.Wrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersistRelationFilter<R extends SkysailServerResource<?>, T extends Entity> extends
        AbstractResourceFilter<R, T> {

    public PersistRelationFilter(SkysailApplication skysailApplication) {
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        List<T> entity = (List<T>) responseWrapper.getEntity();
        ((PostRelationResource) resource).addRelations(entity);
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
