package io.skysail.server.app.demo;

import org.restlet.resource.Get;

import io.skysail.api.links.LinkRelation;
import io.skysail.server.ResourceContextId;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class RamlClientResource extends SkysailServerResource<String> {
	
	public RamlClientResource() {
		addToContext(ResourceContextId.LINK_TITLE, "help");
	}

	@Override
	@Get
	public String getEntity() {
		return null;
	}

	@Override
	public LinkRelation getLinkRelation() {
		return LinkRelation.COLLECTION;
	}

}
