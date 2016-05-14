package io.skysail.server.restlet.filter;

import java.util.List;

import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.PostRelationResource;
import io.skysail.server.restlet.resources.SkysailServerResource;
import io.skysail.server.restlet.response.Wrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersistRelationFilter<R extends SkysailServerResource<?>, T extends Identifiable> extends
        AbstractResourceFilter<R, T> {

    public PersistRelationFilter(SkysailApplication skysailApplication) {
        // eventHelper = new EventHelper(skysailApplication.getEventAdmin());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        List<T> entity = (List<T>) responseWrapper.getEntity();
        ((PostRelationResource) resource).addRelations(entity);
//        String id = ((T)entity).getId();
//        if (id != null) {
//            response.setLocationRef(response.getRequest().getResourceRef().addSegment(id.replace("#", "")));
//        }
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
