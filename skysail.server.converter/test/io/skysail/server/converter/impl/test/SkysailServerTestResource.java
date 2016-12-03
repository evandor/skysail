package io.skysail.server.converter.impl.test;

import org.mockito.Mockito;
import org.osgi.framework.Bundle;

import io.skysail.api.links.LinkRelation;
import io.skysail.domain.Identifiable;
import io.skysail.server.app.SkysailApplication;
import io.skysail.server.restlet.resources.SkysailServerResource;

public class SkysailServerTestResource extends SkysailServerResource<Identifiable> {

	public SkysailServerTestResource() {
		setApplication(new SkysailApplication("theApp") {
			
			private Bundle bundle = Mockito.mock(Bundle.class);
			
			@Override
			public Bundle getBundle() {
				return this.bundle;
			}
		});
	}

	@Override
	public Identifiable getEntity() {
		return null;
	}

	@Override
	public LinkRelation getLinkRelation() {
		return null;
	}

}
