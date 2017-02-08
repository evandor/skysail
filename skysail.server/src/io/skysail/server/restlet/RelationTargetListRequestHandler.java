package io.skysail.server.restlet;

import org.restlet.data.Method;

import io.skysail.core.app.SkysailApplication;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.filter.*;
import io.skysail.server.restlet.resources.PostRelationResource;

public class RelationTargetListRequestHandler<FROM extends Entity, TO extends Entity> { // NOSONAR

    private SkysailApplication application;

    public RelationTargetListRequestHandler(SkysailApplication application) {
        this.application = application;
    }

    public synchronized AbstractResourceFilter<PostRelationResource<FROM,TO>, TO> createForRelationTargetList(Method method) {
        if (method.equals(Method.GET)) {
            return chainForRelationTargetListGet();
        } else if (method.equals(Method.POST)) {
        }
        throw new RuntimeException("Method " + method + " is not yet supported");
    }

   
    private AbstractResourceFilter<PostRelationResource<FROM,TO>, TO> chainForRelationTargetListGet() {
        return new ExceptionCatchingFilter<PostRelationResource<FROM,TO>, TO>(application)
                .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new DataExtractingFilter<>())
                .calling(new AddLinkheadersFilter<>())
                .calling(new SetExecutionTimeInResponseFilter<>())
                .calling(new RedirectFilter<>());
    }
    


    public AbstractResourceFilter<PostRelationResource<FROM,TO>, TO> createForPost() {
        return new ExceptionCatchingFilter<PostRelationResource<FROM,TO>, TO>(application)
               .calling(new ExtractStandardQueryParametersResourceFilter<>())
                .calling(new CheckInvalidInputFilter<>(application))
                .calling(new FormDataExtractingFilter<>())
//                .calling(new CheckBusinessViolationsFilter<>(application))
                .calling(new PersistRelationFilter<>(application))
                //.calling(new EntityWasAddedFilter<>(application))
                .calling(new AddLinkheadersFilter<>())
                .calling(new PostRedirectGetFilter<>());
    }

}
