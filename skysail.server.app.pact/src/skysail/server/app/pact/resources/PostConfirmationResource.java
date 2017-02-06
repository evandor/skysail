package skysail.server.app.pact.resources;

import java.util.Date;

import io.skysail.server.restlet.resources.PostEntityServerResource;
import skysail.server.app.pact.PactApplication;
import skysail.server.app.pact.domain.Confirmation;

public class PostConfirmationResource extends PostEntityServerResource<Confirmation> {

	private PactApplication app;

	@Override
	protected void doInit() {
		app = (PactApplication) getApplication();
	}

	@Override
	public Confirmation createEntityTemplate() {
        return new Confirmation();
	}

	@Override
	public void addEntity(Confirmation entity) {
		entity.setCreated(new Date());
		app.getConfRepo().save(entity, getApplicationModel());
	}

}
