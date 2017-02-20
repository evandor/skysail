package io.skysail.server.restlet.filter;

import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;


public interface HookFilter<R extends SkysailServerResource<T>,T extends Entity> {

    AbstractResourceFilter<R,T> getFilter();

}
