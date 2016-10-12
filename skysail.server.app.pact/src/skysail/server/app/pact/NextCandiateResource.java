package skysail.server.app.pact;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;

public class NextCandiateResource extends EntityServerResource<Candiate>{

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

    @Override
    public Candiate getEntity() {
        return new Candiate();
    }

}
