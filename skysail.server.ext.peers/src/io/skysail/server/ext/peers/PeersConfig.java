package io.skysail.server.ext.peers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, configurationPolicy = ConfigurationPolicy.OPTIONAL, service = PeersConfig.class, configurationPid="peers")
@Slf4j
public class PeersConfig {

    private volatile List<Peer> peers = new ArrayList<>();

    @Activate
    public void activate(Map<String, String> config) {
        log.info("activating peers extension {}", this.getClass().getName());
        if (config == null) {
            return;
        }
        readConfig(config);
    }

    public void deactivate() {
        log.info("deactivating peers extension {}", this.getClass().getName());
        peers = new ArrayList<>();
    }

    public List<Peer> getPeers() {
        return Collections.unmodifiableList(peers );
    }

    private void readConfig(Map<String, String> config) {
        Set<String> configIdentifiers = new HashSet<>();
        config.keySet().stream()
        	.filter(key -> !"felix.fileinstall.filename".equals(key))
        	.filter(key -> !"component.name".equals(key))
        	.filter(key -> !"service.pid".equals(key))
        	.filter(key -> !"component.id".equals(key))
        	.map(key -> key.split("\\.")[0]).forEach(configIdentifiers::add);
        configIdentifiers.stream().forEach(i -> createPeer(config, i));
    }

	private void createPeer(Map<String, String> config, String i) {
		try {
			String ip = config.get(i+".ip");
			String portAsString = config.get(i+".port");
			log.info("about to create peer configuration for '{}' with ip {}, port {}", i, ip, portAsString);
			peers.add(new Peer(i,ip,portAsString != null ? Integer.parseInt(portAsString) : null));
		} catch (Exception e) { // NOSONAR
			log.error(e.getMessage());
		}
    }


}
