package io.skysail.server.model.test;

import org.mockito.Mockito;

import io.skysail.core.app.SkysailApplication;
import io.skysail.server.restlet.resources.EntityServerResource;

public class TestEntityResource extends EntityServerResource<TestEntity>{

    @Override
    public TestEntity getEntity() {
        return new TestEntity();
    }

    @Override
    public SkysailApplication getApplication() {
        return Mockito.mock(SkysailApplication.class);
    }

}
