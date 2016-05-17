package io.skysail.server.ext.peers.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import io.skysail.server.ext.peers.PeersConfig;

public class PeersConfigTest {

    private PeersConfig peersConfig;

    @Before
    public void setup() {
        peersConfig = new PeersConfig();
    }

    @Test
    public void activated_with_null_yields_emptyPeersList() {
        peersConfig.activate(null);
        assertThat(peersConfig.getPeers().size(),is(0));
    }

    @Test
    public void activated_with_emptyMap_yields_emptyPeersList() {
        peersConfig.activate(Collections.emptyMap());
        assertThat(peersConfig.getPeers().size(),is(0));
    }

    @Test
    public void activated_with_element_yields_peersList_with_that_element() {
        Map<String, String> config = new HashMap<>();
        config.put("parent.id", "the parent");
        config.put("parent.ip", "123.123.123.123");
        config.put("parent.port", "8888");
        peersConfig.activate(config);
        assertThat(peersConfig.getPeers().size(),is(1));
    }
}
