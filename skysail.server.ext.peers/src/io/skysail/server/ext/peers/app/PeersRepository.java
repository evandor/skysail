package io.skysail.server.ext.peers.app;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;

@Component(immediate = true, property = "name=PeersRepository")
public class PeersRepository extends GraphDbRepository<PublicPeerDescription>
        implements io.skysail.domain.core.repos.DbRepository {

    @Reference
    public void setDbService(DbService dbService) {
        this.dbService = dbService;
    }

    public void unsetDbService(DbService dbService) {
        this.dbService = null;
    }

    @Activate
    public void activate() {
        dbService.createWithSuperClass("V", DbClassName.of(PublicPeerDescription.class));
        dbService.register(PublicPeerDescription.class);
    }

    public PublicPeerDescription findByPeerIdentifier(String identifier) {
        String sql = "SELECT from " + DbClassName.of(PublicPeerDescription.class)
                + " WHERE peerIdentifier= :peerIdentifier";
        Map<String, Object> params = new HashMap<>();
        params.put("peerIdentifier", identifier);
        List<PublicPeerDescription> found = dbService.findGraphs(PublicPeerDescription.class, sql, params);
        if (found.size() == 1) {
            return found.get(0);
        }
        return null;
    }

}