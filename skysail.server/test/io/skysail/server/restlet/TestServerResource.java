package io.skysail.server.restlet;

import io.skysail.api.doc.ApiMetadata;
import io.skysail.api.links.LinkRelation;
import io.skysail.core.resources.SkysailServerResource;
import io.skysail.domain.Entity;

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
	public ApiMetadata getApiMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

}