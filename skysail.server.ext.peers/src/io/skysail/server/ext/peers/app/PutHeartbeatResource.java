package io.skysail.server.ext.peers.app;

import org.restlet.resource.ResourceException;

import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutHeartbeatResource extends PutEntityServerResource<PublicPeerDescription> {

	private PeersApplication app;

	@Override
	protected void doInit() throws ResourceException {
		app = (PeersApplication)getApplication();
	}
	@Override
	public PublicPeerDescription getEntity() {
		PublicPeerDescription existingEntry = (PublicPeerDescription)app.getRepository(PublicPeerDescription.class).findOne(getAttribute("id"));
		if (existingEntry != null) {
			return existingEntry;
		}
		return new PublicPeerDescription();
	}
	
	@Override
	public void updateEntity(PublicPeerDescription entity) {
		entity.setId(getAttribute("id"));
		super.updateEntity(entity);
	}

}
