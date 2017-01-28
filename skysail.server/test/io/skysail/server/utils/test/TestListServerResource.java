package io.skysail.server.utils.test;

import java.util.Arrays;
import java.util.List;

import org.mockito.Mockito;

import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.ListServerResource;

public class TestListServerResource extends ListServerResource<Identifiable> {

    @Override
    public List<Identifiable> getEntity() {
        return Arrays.asList(new Identifiable() {

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