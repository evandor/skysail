package io.skysail.doc.swagger;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class SwaggerResource extends EntityServerResource<SwaggerSpec> {

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public SwaggerSpec getEntity() {
        return new SwaggerSpec(getApplication(), getRequest());
    }

}