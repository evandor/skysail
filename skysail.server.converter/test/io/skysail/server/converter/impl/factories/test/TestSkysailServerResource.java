package io.skysail.server.converter.impl.factories.test;

import java.util.Map;

import org.restlet.data.Method;

import io.skysail.api.links.LinkRelation;
import io.skysail.core.resources.SkysailServerResource;

class TestSkysailServerResource extends SkysailServerResource<TestEntity> {
    @Override
    public TestEntity getEntity() {
        return null;
    }
    @Override
    public LinkRelation getLinkRelation() {
        return null;
    }
	@Override
	public Map<Method, Map<String, Object>> getApiMetadata() {
		return null;
	}
}