package skysail.server.app.pact;


import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;
import lombok.extern.slf4j.Slf4j;

@Component(immediate = true, property = "name=PactRepository")
@Slf4j
public class PactRepository extends GraphDbRepository<Pact> implements DbRepository {

//    @Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MANDATORY)
//    private DbService dbService;

    @Reference
    public void setDbService(DbService dbService) {
        super.setDbService(dbService);
    }

    public void unsetDbService(DbService dbService) {
        super.unsetDbService(dbService);
    }

    @Activate
    public void activate() {
    	log.info("activating {}", this.getClass().getName());
        dbService.createWithSuperClass("V", DbClassName.of(Pact.class));
        dbService.register(Pact.class);
    }

}