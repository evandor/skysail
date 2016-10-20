package io.skysail.server.ext.elasticsearch.local;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.NodeBuilder;
import org.osgi.service.component.annotations.Component;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@Component(immediate = true)
public class TestES {

	public void activate() {
		// @formatter:off
		Settings.Builder elasticsearchSettings = Settings.settingsBuilder()
                .put("number_of_shards", 1)
                .put("number_of_replicas", 0)
                .put("index.store.type", "ram")
                .put("discovery.zen.ping.multicast.enabled", false)
                .put("gateway.type", "none")
                .put("path.data", "build/es-path")
                .put("path.logs", "build/es-path")
                .put("path.work", "build/es-path")
                .put("path.home", "build/es-path")
                .put("path.conf", "build/es-path")
                .put("transport.tcp.port", 9300)
                .put("network.host: localhost")
                .put("http.enabled", "false") // TODO must be false in prod
                .put("cluster.name", "mp-in-memory")
                .put("client.transport.ping_timeout", "30s"); // 1
//            .put("path.data", tmpDir.toAbsolutePath().toString()) // 2
//            .put("path.home", "PATH_TO_YOUR_ELASTICSEARCH_DIRECTORY"); // 3

        // @formatter:on

          new ElasticsearchTemplate(new NodeBuilder()
            .local(true)
            .settings(elasticsearchSettings.build())
            .node().client());
	}
}
