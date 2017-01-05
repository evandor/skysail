package io.skysail.server.app.notes.resources;

import io.skysail.domain.GenericIdentifiable;
import io.skysail.server.restlet.resources.PostEntityServerResource;

public class SyncRequestResource extends PostEntityServerResource<GenericIdentifiable> {

    @Override
    public GenericIdentifiable createEntityTemplate() {
        return new GenericIdentifiable();
    }

    @Override
    public void addEntity(GenericIdentifiable entity) {
        System.out.println(entity);
        String type = getRequest().getHeaders().getFirstValue("x-amz-sns-message-type");
        System.out.println(type);
    }

}
