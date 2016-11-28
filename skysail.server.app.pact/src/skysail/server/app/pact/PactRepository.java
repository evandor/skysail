package skysail.server.app.pact;


import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PactRepository extends GraphDbRepository<Pact> implements DbRepository {

    public PactRepository(DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
    	log.info("activating {}", this.getClass().getName());
        dbService.createWithSuperClass("V", DbClassName.of(Pact.class));
        dbService.register(Pact.class);
    }

}