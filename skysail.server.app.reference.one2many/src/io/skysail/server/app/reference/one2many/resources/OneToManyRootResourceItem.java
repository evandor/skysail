package io.skysail.server.app.reference.one2many.resources;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class OneToManyRootResourceItem extends EntityServerResource<Id2>{

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Id2 getEntity() {
        // TODO Auto-generated method stub
        return null;
    }

}
