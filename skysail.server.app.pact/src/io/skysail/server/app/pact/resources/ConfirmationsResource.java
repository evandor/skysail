package io.skysail.server.app.pact.resources;

import java.util.List;

import org.restlet.resource.ResourceException;

import io.skysail.server.app.pact.PactApplication;
import io.skysail.server.app.pact.domain.Confirmation;
import io.skysail.server.queryfilter.filtering.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

public class ConfirmationsResource extends ListServerResource<Confirmation> {

	private PactApplication app;

	@Override
	protected void doInit() throws ResourceException {
		app = (PactApplication)getApplication();
	}
	
	@Override
	public List<?> getEntity() {
		return app.getConfRepo().find(new Filter(getRequest()));
	}

}
