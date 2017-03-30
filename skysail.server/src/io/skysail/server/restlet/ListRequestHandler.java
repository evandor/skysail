package io.skysail.server.restlet;

import org.restlet.data.Method;

import io.skysail.core.app.SkysailApplication;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.filter.AbstractListResourceFilter;
import io.skysail.server.restlet.filter.AddLinkheadersListFilter;
import io.skysail.server.restlet.filter.DataExtractingListFilter;
import io.skysail.server.restlet.filter.ExceptionCatchingListFilter;
import io.skysail.server.restlet.filter.RedirectListFilter;
import io.skysail.server.restlet.filter.SetExecutionTimeInListResponseFilter;

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
    public synchronized AbstractListResourceFilter createForList(Method method) {
        if (method.equals(Method.GET)) {
            return chainForListGet();
        } else if (method.equals(Method.POST)) {
            return chainForListPost();
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

    private AbstractListResourceFilter chainForListPost() {
        return new ExceptionCatchingListFilter(application)
//                .calling(new ExtractStandardQueryParametersResourceFilter<>())
//                .calling(new CheckInvalidInputFilter<>())
//                .calling(new FormDataExtractingFilter<>())
//                .calling(new CheckBusinessViolationsFilter<>(application))
//                .calling(new PersistEntityFilter<>(application))
                ;
    }

    private AbstractListResourceFilter chainForListGet() {
        return new ExceptionCatchingListFilter(application)
                // .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new DataExtractingListFilter())
                .calling(new AddLinkheadersListFilter())
                .calling(new SetExecutionTimeInListResponseFilter())
                .calling(new RedirectListFilter())
                ;
    }

}
