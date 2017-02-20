package io.skysail.server;

import java.util.Map;

import org.restlet.data.Method;

import io.skysail.api.links.LinkRelation;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;
import io.skysail.server.domain.jvm.ResourceType;

public class TestServerResource extends SkysailServerResource<Entity> {

    public TestServerResource() {
        super();
    }

    @Override
    public LinkRelation getLinkRelation() {
        return LinkRelation.ITEM;
    }

    @Override
    public Entity getEntity() {
        return null;
    }

    @Override
    public ResourceType getResourceType() {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public Map<Method, Map<String, Object>> getApiMetadata() {
		return null;
	}

}