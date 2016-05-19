package io.skysail.server.ext.peers.app;

import java.util.Date;

import io.skysail.server.restlet.resources.PutEntityServerResource;

public class PutHeartbeatResource extends PutEntityServerResource<PublicPeerDescription> {

    private PeersApplication app;

    @Override
    protected void doInit() {
        app = (PeersApplication) getApplication();
    }

    @Override
    public PublicPeerDescription getEntity() {
        PeersRepository repository = (PeersRepository) app.getRepository(PublicPeerDescription.class);
        // (PublicPeerDescription)app.getRepository(PublicPeerDescription.class).findOne(getAttribute("id"));
        PublicPeerDescription existingEntry = repository.findByPeerIdentifier(getAttribute("id"));
        if (existingEntry != null) {
            return existingEntry;
        }
        return null;
    }

    @Override
    public void updateEntity(PublicPeerDescription entity) {
       // entity.setId(getAttribute("id"));
        entity.setPinged(new Date());
        super.updateOrCreateEntity(entity);
    }

}
