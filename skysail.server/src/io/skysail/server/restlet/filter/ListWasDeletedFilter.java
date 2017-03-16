package io.skysail.server.restlet.filter;

import java.util.List;

import io.skysail.core.app.SkysailApplication;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.response.Wrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListWasDeletedFilter<R extends SkysailServerResource<List<T>>, T extends Entity> extends AbstractListResourceFilter<R, T> {

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
