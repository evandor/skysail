package io.skysail.server.app.pact;


import io.skysail.domain.core.repos.DbRepository;
import io.skysail.server.app.pact.domain.Confirmation;
import io.skysail.server.db.DbClassName;
import io.skysail.server.db.DbService;
import io.skysail.server.db.GraphDbRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfirmationRepository extends GraphDbRepository<Confirmation> implements DbRepository {

    public ConfirmationRepository(DbService dbService) {
        this.dbService = dbService;
        activate();
    }

    public void activate() {
    	log.info("activating {}", this.getClass().getName());
        dbService.createWithSuperClass("V", DbClassName.of(Confirmation.class));
        dbService.register(Confirmation.class);
    }

}