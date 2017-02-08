package io.skysail.server.utils.test;

import java.util.Arrays;
import java.util.List;

import org.mockito.Mockito;

import io.skysail.core.app.SkysailApplication;
import io.skysail.domain.Entity;
import io.skysail.server.restlet.resources.ListServerResource;

public class TestListServerResource extends ListServerResource<Entity> {

    @Override
    public List<Entity> getEntity() {
        return Arrays.asList(new Entity() {

            @Override
            public String getId() {
                return null;
            }

        });
    }

    @Override
    public SkysailApplication getApplication() {
        return Mockito.mock(SkysailApplication.class);
    }


}