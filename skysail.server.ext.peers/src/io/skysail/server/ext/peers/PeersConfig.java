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
        config.keySet().stream().forEach(configIdentifiers::add);
        configIdentifiers.stream().forEach(i -> peers.add(new Peer()));
    }


}
