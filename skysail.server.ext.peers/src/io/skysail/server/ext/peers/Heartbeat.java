package io.skysail.server.ext.peers;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.restlet.resource.ClientResource;

import io.skysail.server.ext.peers.app.PublicPeerDescription;
import io.skysail.server.product.ProductDefinition;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true)
@Slf4j
public class Heartbeat {

	private TimerTask timerTask;

	public class MyTimerTask extends TimerTask {

		private List<Peer> peers;
		
		private String installationPublicKeyHash;
		
		private ProductDefinition productDefinition;

		public MyTimerTask(List<Peer> peers, ProductDefinition productDefinition) {
			this.peers = peers;
			this.productDefinition = productDefinition;
			try {
				installationPublicKeyHash = productDefinition.installationPublicKeyHash();
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
		}

		@Override
		public void run() {
			if (installationPublicKeyHash == null) {
				log.error("error");
				return;
			}
			peers.stream().forEach(peer -> {
				try {
					String target = new StringBuilder(
							peer.getServer())
							.append("/peers/v1/heartbeat/")
							.append(installationPublicKeyHash).append("/").toString();
					ClientResource cr = new ClientResource(target);
					PublicPeerDescription publicPeerDescription = new PublicPeerDescription();
					publicPeerDescription.setPublicKey(productDefinition.installationPublicKey());
					cr.put(publicPeerDescription);
				} catch (Exception e) {
					log.warn(e.getMessage());
				}
			});
		}

	}

	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	private volatile PeersConfig peersConfig;
	
	@Reference(cardinality = ReferenceCardinality.MANDATORY)
	private volatile ProductDefinition productDefinition;

	@Activate
	public void activate() {
		timerTask = new MyTimerTask(peersConfig.getPeers(), productDefinition);
		new Timer(true).scheduleAtFixedRate(timerTask, 10000, 60000);
	}

	@Deactivate
	public void deactivate() {
		timerTask.cancel();
	}
}
