package io.skysail.server.model.test;

import java.util.Map;

import org.mockito.Mockito;
import org.restlet.data.Method;

import io.skysail.core.app.SkysailApplication;
import io.skysail.server.restlet.resources.PutEntityServerResource;

public class TestPostResource extends PutEntityServerResource<TestEntity>{

    @Override
    public void updateEntity(TestEntity entity) {
    }

    @Override
    public TestEntity getEntity() {
        return null;
    }

    @Override
    public SkysailApplication getApplication() {
        return Mockito.mock(SkysailApplication.class);
    }

    @Override
    public TestEntity createEntityTemplate() {
        return new TestEntity();
    }

	@Override
	public Map<Method, Map<String, Object>> getApiMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

}
