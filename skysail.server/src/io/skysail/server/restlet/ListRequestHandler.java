package io.skysail.server.restlet;

import java.util.List;

import org.restlet.data.Method;

import io.skysail.core.app.SkysailApplication;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.filter.AbstractListResourceFilter;
import io.skysail.server.restlet.filter.ExceptionCatchingListFilter;

public class ListRequestHandler<T extends Entity> {

    private SkysailApplication application;

    public ListRequestHandler(SkysailApplication application) {
        this.application = application;
    }

    /**
     * for now, always return new objects
     *
     * @param method
     *            http method
     * @return chain
     */
    public synchronized AbstractListResourceFilter<SkysailServerResource<List<T>>, T> createForList(Method method) {
        if (method.equals(Method.GET)) {
            return chainForListGet();
        } else if (method.equals(Method.POST)) {
            return chainForListPost();
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

    private AbstractListResourceFilter<SkysailServerResource<List<T>>, T> chainForListPost() {
        return new ExceptionCatchingListFilter<>(application)
//                .calling(new ExtractStandardQueryParametersResourceFilter<>())
//                .calling(new CheckInvalidInputFilter<>())
//                .calling(new FormDataExtractingFilter<>())
//                .calling(new CheckBusinessViolationsFilter<>(application))
//                .calling(new PersistEntityFilter<>(application))
                ;
    }

    private AbstractListResourceFilter<SkysailServerResource<List<T>>, T> chainForListGet() {
        return new ExceptionCatchingListFilter<>(application)
//                .calling(new ExtractStandardQueryParametersResourceFilter<>())
//                .calling(new DataExtractingFilter<>())
//                .calling(new AddLinkheadersFilter<>())
//                .calling(new SetExecutionTimeInResponseFilter<>())
//                .calling(new RedirectFilter<>())
                ;
    }

}
