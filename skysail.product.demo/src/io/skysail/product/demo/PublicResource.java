package io.skysail.product.demo;

import java.util.Collections;
import java.util.List;

import io.skysail.server.restlet.resources.ListServerResource;

public class PublicResource extends ListServerResource<Empty> {

    @Override
    public List<Empty> getEntity() {
        return Collections.emptyList();
    }

}
