package io.skysail.server.converter.impl.test;

import java.util.List;

import io.skysail.server.restlet.resources.ListServerResource;

public class ATestResource extends ListServerResource<AnEntity> {

    @Override
    public List<AnEntity> getEntity() {
        return null;
    }

}
