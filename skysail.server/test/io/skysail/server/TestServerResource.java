package io.skysail.server;

import io.skysail.api.doc.ApiMetadata;
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
	public ApiMetadata getApiMetadata() {
		return null;
	}

}