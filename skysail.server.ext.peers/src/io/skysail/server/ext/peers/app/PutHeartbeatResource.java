package io.skysail.server.ext.peers.app;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import io.skysail.server.product.ProductDefinition;
import io.skysail.server.restlet.resources.PutEntityServerResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PutHeartbeatResource extends PutEntityServerResource<PublicPeerDescription> {

    private PeersApplication app;

    @Override
    protected void doInit() {
        app = (PeersApplication) getApplication();
    }

    @Override
    public PublicPeerDescription getEntity() {
        PeersRepository repository = null;//(PeersRepository) app.getRepository(PublicPeerDescription.class);
        PublicPeerDescription existingEntry = repository.findByPeerIdentifier(getAttribute("id"));
        if (existingEntry != null) {
            return existingEntry;
        }
        return null;
    }

    @Override
    public void updateEntity(PublicPeerDescription entityFromRequest) {
    	PublicPeerDescription entity = getEntity();
    	if (entity != null) {
    		entityFromRequest.setId(entity.getId());
    		copyProperties(entity,entityFromRequest);
    	} else {
    		entity = entityFromRequest;
    	}

    	entity.setPinged(new Date());
        try {
			String address = getRequest().getClientInfo().getAddress();
			validateAndSet(address, entity);
			entity.setPort(getRequest().getClientInfo().getPort());
			validateHash(entity);
		} catch (UnknownHostException e) {
			log.warn(e.getMessage());
		}
        //app.getRepository(PublicPeerDescription.class).update(entity,app.getApplicationModel());
    }

	private void validateAndSet(String address, PublicPeerDescription entity) throws UnknownHostException {
		InetAddress.getByName(address);
		entity.setIp(address);
	}

	private void validateHash(PublicPeerDescription entity) {
		String peerIdentifier = entity.getPeerIdentifier();

		ProductDefinition checkProductDefinition = new ProductDefinition() {

			@Override
			public byte[] installationPublicKey() {
				return  entity.getPublicKey();
			}

			@Override
			public byte[] installationPrivateKey() {
				return null;
			}
		};

		entity.setStatus(PeerStatus.INVALID);
		try {
			if (checkProductDefinition.installationPublicKeyHash().equals(peerIdentifier)) {
				entity.setStatus(PeerStatus.VALID);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

	}


}
