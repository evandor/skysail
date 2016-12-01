package io.skysail.server.converter.impl.test;

import io.skysail.api.links.LinkRelation;
import io.skysail.domain.Identifiable;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class SkysailServerTestResource extends SkysailServerResource<Identifiable> {

	@Override
	public Identifiable getEntity() {
		return null;
	}

	@Override
	public LinkRelation getLinkRelation() {
		return null;
	}

}
