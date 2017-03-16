package io.skysail.server.app.pact;

import io.skysail.server.restlet.resources.EntityServerResource;

public class NextCandiateResource extends EntityServerResource<Candiate>{

    @Override
    public Candiate getEntity() {
        return new Candiate();
    }

}
