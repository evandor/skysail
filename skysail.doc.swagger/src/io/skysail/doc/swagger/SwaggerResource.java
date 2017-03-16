package io.skysail.doc.swagger;

import io.skysail.server.restlet.resources.EntityServerResource;

public class SwaggerResource extends EntityServerResource<SwaggerSpec> {

    @Override
    public SwaggerSpec getEntity() {
        return new SwaggerSpec(getApplication(), getRequest());
    }

}
