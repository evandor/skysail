package io.skysail.server.restlet.filter;

import io.skysail.domain.Entity;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.restlet.response.Wrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListWasDeletedFilter<R extends ListServerResource<T>, T extends Entity> extends AbstractResourceFilter<R, T> {

    public ListWasDeletedFilter(SkysailApplication skysailApplication) {
    }

    @Override
    public FilterResult doHandle(R resource, Wrapper<T> responseWrapper) {
        log.debug("entering {}#doHandle", this.getClass().getSimpleName());
        String infoMessage = resource.getClass().getSimpleName() + ".deleted.success";
        responseWrapper.addInfo(infoMessage);
        super.doHandle(resource, responseWrapper);
        return FilterResult.CONTINUE;
    }
}
