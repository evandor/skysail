package io.skysail.server.um.aws;

import io.skysail.api.responses.SkysailResponse;
import io.skysail.server.restlet.resources.EntityServerResource;
import io.skysail.server.um.domain.Credentials;

public class AwsLogoutPage extends EntityServerResource<Credentials> {

    @Override
    public Credentials getEntity() {
        return new Credentials();
    }

    @Override
    public SkysailResponse<?> eraseEntity() {
        return null;
    }

}
