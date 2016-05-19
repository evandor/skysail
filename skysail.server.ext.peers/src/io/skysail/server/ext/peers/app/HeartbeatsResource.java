package io.skysail.server.ext.peers.app;

import java.util.List;

import io.skysail.server.queryfilter.Filter;
import io.skysail.server.restlet.resources.ListServerResource;

public class HeartbeatsResource extends ListServerResource<PublicPeerDescription> {

	private PeersApplication app;

	@Override
	protected void doInit() {
		app = (PeersApplication) getApplication();
	}

	@Override
	public List<?> getEntity() {
		PeersRepository repository = (PeersRepository) app.getRepository(PublicPeerDescription.class);
		return repository.find(new Filter(getRequest()));
	}

}
