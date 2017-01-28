package io.skysail.server.restlet.filter;

import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.SkysailServerResource;


public interface HookFilter<R extends SkysailServerResource<T>,T extends Entity> {

    AbstractResourceFilter<R,T> getFilter();

}
