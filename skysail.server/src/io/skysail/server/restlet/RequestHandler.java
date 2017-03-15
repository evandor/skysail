package io.skysail.server.restlet;

import org.restlet.data.Method;

import io.skysail.core.app.SkysailApplication;
import io.skysail.domain.Entity;
import io.skysail.domain.GenericIdentifiable;
import io.skysail.server.restlet.filter.AbstractResourceFilter;
import io.skysail.server.restlet.filter.AddLinkheadersFilter;
import io.skysail.server.restlet.filter.AddReferrerCookieFilter;
import io.skysail.server.restlet.filter.CheckBusinessViolationsFilter;
import io.skysail.server.restlet.filter.CheckInvalidInputFilter;
import io.skysail.server.restlet.filter.DataExtractingFilter;
import io.skysail.server.restlet.filter.DeleteEntityFilter;
import io.skysail.server.restlet.filter.DeleteListFilter;
import io.skysail.server.restlet.filter.DeleteListRedirectGetFilter;
import io.skysail.server.restlet.filter.DeleteRedirectGetFilter;
import io.skysail.server.restlet.filter.EntityWasAddedFilter;
import io.skysail.server.restlet.filter.EntityWasDeletedFilter;
import io.skysail.server.restlet.filter.ExceptionCatchingFilter;
import io.skysail.server.restlet.filter.ExtractStandardQueryParametersResourceFilter;
import io.skysail.server.restlet.filter.FormDataExtractingFilter;
import io.skysail.server.restlet.filter.ListWasDeletedFilter;
import io.skysail.server.restlet.filter.PatchEntityFilter;
import io.skysail.server.restlet.filter.PersistEntityFilter;
import io.skysail.server.restlet.filter.PostRedirectGetFilter;
import io.skysail.server.restlet.filter.PutRedirectGetFilter;
import io.skysail.server.restlet.filter.UpdateEntityFilter;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.restlet.resources.JsonServerResource;
import io.skysail.server.restlet.resources.ListServerResource;
import io.skysail.server.restlet.resources.PatchEntityServerResource;
import io.skysail.server.restlet.resources.PostEntityServerResource;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class RequestHandler<T extends Entity> {

    private SkysailApplication application;

    public RequestHandler(SkysailApplication application) {
        this.application = application;
    }

    /**
     * for now, always return new objects
     *
     * @param method
     *            http method
     * @return the chain
     */
    public AbstractResourceFilter<EntityServerResource<T>, T> createForEntity(Method method) {
        if (method.equals(Method.GET)) {
            return chainForEntityGet();
        } else if (method.equals(Method.DELETE)) {
            return chainForEntityDelete();
        }

        throw new RuntimeException("Method " + method + " is not yet supported");
    }

    public AbstractResourceFilter<JsonServerResource, GenericIdentifiable> createForJson(Method method) {
        if (method.equals(Method.GET)) {
            return chainForJsonGet();
//        } else if (method.equals(Method.DELETE)) {
//            return chainForEntityDelete();
        }

        throw new RuntimeException("Method " + method + " is not yet supported");
    }

    public  AbstractResourceFilter<PutEntityServerResource<T>, T> createForFormResponse() {
        return new ExceptionCatchingFilter<PutEntityServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new DataExtractingFilter<>())
                .calling(new AddLinkheadersFilter<>());
    }

    /**
     * for now, always return new objects
     *
     * @return the filter chain
     */
    public AbstractResourceFilter<PostEntityServerResource<T>, T> createForPost() {
        return chainForEntityPost();
    }

    /**
     * for now, always return new objects
     *
     */
    public AbstractResourceFilter<PutEntityServerResource<T>, T> createForPut() {
        return chainForEntityPut();
    }

    public AbstractResourceFilter<PatchEntityServerResource<T>, T> createForPatch() {
        return chainForEntityPatch();
    }

    public AbstractResourceFilter<ListServerResource<T>, T> createForListDelete() {
        return chainForListDelete();
    }

    private AbstractResourceFilter<JsonServerResource, GenericIdentifiable> chainForJsonGet() {
        return new ExceptionCatchingFilter<>(application)
//                .calling(new ExtractStandardQueryParametersResourceFilter<>())
//                .calling(new JsonExtractingFilter<>())
//                .calling(new AddReferrerCookieFilter<>())
//                .calling(new AddLinkheadersFilter<>())
                ;
    }

    private AbstractResourceFilter<EntityServerResource<T>, T> chainForEntityGet() {
        return new ExceptionCatchingFilter<EntityServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new DataExtractingFilter<>())
                .calling(new AddReferrerCookieFilter<>())
                .calling(new AddLinkheadersFilter<>());
    }

    private AbstractResourceFilter<PostEntityServerResource<T>, T> chainForEntityPost() {
        return new ExceptionCatchingFilter<PostEntityServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new CheckInvalidInputFilter<>(application))
                .calling(new FormDataExtractingFilter<>())
                .calling(new CheckBusinessViolationsFilter<>(application))
                .calling(new PersistEntityFilter<>(application))
                .calling(new EntityWasAddedFilter<>(application))
                .calling(new AddLinkheadersFilter<>())
                .calling(new PostRedirectGetFilter<>());
    }

    private AbstractResourceFilter<PutEntityServerResource<T>, T> chainForEntityPut() {
        return new ExceptionCatchingFilter<PutEntityServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new CheckInvalidInputFilter<>(application))
                .calling(new FormDataExtractingFilter<>())
                .calling(new CheckBusinessViolationsFilter<>(application))
                .calling(new UpdateEntityFilter<>())
                .calling(new EntityWasAddedFilter<>(application))
                .calling(new AddLinkheadersFilter<>())
                .calling(new PutRedirectGetFilter<>());
    }

    private AbstractResourceFilter<PatchEntityServerResource<T>, T> chainForEntityPatch() {
        return new ExceptionCatchingFilter<PatchEntityServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new CheckInvalidInputFilter<>(application))
                .calling(new FormDataExtractingFilter<>())
                //.calling(new CheckBusinessViolationsFilter<>(application))
                .calling(new PatchEntityFilter<PatchEntityServerResource<T>, T>())
                //.calling(new EntityWasPatchedFilter<>(application))
                .calling(new AddLinkheadersFilter<>());
    }

    private AbstractResourceFilter<EntityServerResource<T>, T> chainForEntityDelete() {
        return new ExceptionCatchingFilter<EntityServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new DeleteEntityFilter<>())
                .calling(new EntityWasDeletedFilter<>(application))
                .calling(new DeleteRedirectGetFilter<>());
    }

    private AbstractResourceFilter<ListServerResource<T>, T> chainForListDelete() {
        return new ExceptionCatchingFilter<ListServerResource<T>, T>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new DeleteListFilter<>())
                .calling(new ListWasDeletedFilter<>(application))
                .calling(new DeleteListRedirectGetFilter<>());
    }

    public AbstractResourceFilter<PostEntityServerResource<T>, T> newInstance(Method method) {
        if (method.equals(Method.GET)) {
            return new ExceptionCatchingFilter<PostEntityServerResource<T>, T>(application)
                .calling(new DataExtractingFilter<>())
                .calling(new AddLinkheadersFilter<>());
        }

        throw new RuntimeException("Method " + method + " is not yet supported");
    }

}
